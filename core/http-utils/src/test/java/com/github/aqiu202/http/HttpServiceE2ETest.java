package com.github.aqiu202.http;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.exchange.RestClientExchanger;
import com.github.aqiu202.http.exchange.RestTemplateExchanger;
import com.github.aqiu202.http.exchange.WebClientExchanger;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.util.RetryPolicy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 基于 JDK 内置 {@link HttpServer} 的端到端测试，覆盖三种 Exchanger 的行为一致性。
 */
class HttpServiceE2ETest {

    private static HttpServer server;
    private static String baseUrl;
    private static String otherOriginUrl;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", HttpServiceE2ETest::handle);
        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://localhost:" + port;
        otherOriginUrl = "http://127.0.0.1:" + port;
    }

    @AfterAll
    static void stopServer() {
        server.stop(0);
    }

    static Stream<Named<HttpExchanger>> exchangers() {
        return Stream.of(
                Named.of("RestClient", RestClientExchanger.create()),
                Named.of("RestTemplate", RestTemplateExchanger.create()),
                Named.of("WebClient", new WebClientExchanger()));
    }

    static Stream<Named<HttpExchanger>> allExchangers() {
        return Stream.concat(exchangers(), Stream.of(
                Named.of("StatefulRestClient", RestClientExchanger.stateful()),
                Named.of("StatefulRestTemplate", RestTemplateExchanger.stateful()),
                Named.of("StatefulWebClient", WebClientExchanger.stateful())));
    }

    private static HttpService service(HttpExchanger exchanger) {
        return HttpService.builder().baseUrl(baseUrl).exchanger(exchanger).build();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void multipartStreamFileIsSentIntact(HttpExchanger exchanger) {
        FileParam file = FileParam.of(new ByteArrayInputStream("hello world!".getBytes(UTF_8)),
                "image/png", "a.bin");
        HttpBodyRequest request = service(exchanger).post("/echo")
                .body(new HttpBodyEntity().addParameters("file", file).addParameters("name", "n1"));
        // 打印请求（调试日志）会读取文件大小，不能因此把流读空
        assertNotNull(request.toString());
        String echo = request.exchange();
        assertTrue(echo.contains("hello world!"), echo);
        assertTrue(echo.contains("filename=\"a.bin\""), echo);
        assertTrue(echo.contains("Content-Type: image/png"), echo);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void multipartIsReplayableOnRetry(HttpExchanger exchanger) {
        FileParam file = FileParam.of(new ByteArrayInputStream("hello world!".getBytes(UTF_8)),
                "text/plain", "a.txt");
        AtomicInteger attempts = new AtomicInteger();
        HttpResponseEntity<String> response = service(exchanger).post("/echo")
                .body(new HttpBodyEntity().addParameters("file", file))
                .retry(RetryPolicy.of(r -> attempts.incrementAndGet() == 1, 1))
                .exchangeForResponse();
        assertEquals(2, attempts.get());
        assertTrue(response.getBody().contains("hello world!"), response.getBody());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void formBodyIsUrlEncoded(HttpExchanger exchanger) {
        HttpBodyRequest request = service(exchanger).post("/echo");
        request.getHeaders().form();
        String echo = request.body(new HttpBodyEntity()
                        .addParameters("a", 1)
                        .addParameters("b", "x y", "中"))
                .exchange();
        assertTrue(echo.endsWith("\n\na=1&b=x+y&b=%E4%B8%AD"), echo);
    }

    @Test
    void formBodyRejectsFile() {
        HttpBodyRequest request = service(RestClientExchanger.INSTANCE).post("/echo");
        request.getHeaders().form();
        request.body(new HttpBodyEntity().addParameters("f", FileParam.of(new byte[]{1}, "text/plain", "a.txt")));
        assertThrows(IllegalArgumentException.class, request::exchange);
    }

    @Test
    void nullQueryValuesAreSkipped() {
        String echo = service(RestClientExchanger.INSTANCE).get("/echo")
                .addQueryParam("status", (Object) null)
                .addQueryParam("a", "1", null, "2")
                .exchange();
        assertTrue(echo.contains("URI /echo?a=1&a=2\n"), echo);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void uriVariablesAreEncoded(HttpExchanger exchanger) {
        String echo = service(exchanger).get("/echo/{id}/{name}", "a&b/c d", "中").exchange();
        assertTrue(echo.contains("URI /echo/a%26b%2Fc%20d/%E4%B8%AD\n"), echo);
    }

    @Test
    void missingUriVariablesFailFast() {
        HttpService service = service(RestClientExchanger.INSTANCE);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.get("/echo/{x}{y}", null, "B").getUri());
        assertTrue(e.getMessage().contains("x"), e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> service.get("/echo/{x}/{y}", "A").getUri());
        assertThrows(IllegalArgumentException.class,
                () -> service.getWithVariables("/echo/{id}", Map.of()).getUri());
        assertEquals(baseUrl + "/echo/A/B",
                service.getWithVariables("/echo/{x}/{y}", Map.of("x", "A", "y", "B")).getUriString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void errorStatusIsReturnedAndRetried(HttpExchanger exchanger) {
        AtomicInteger retries = new AtomicInteger();
        HttpResponseEntity<String> response = service(exchanger).get("/status/401")
                .retry(RetryPolicy.of(r -> r.getStatusCode() == 401, 2, r -> retries.incrementAndGet()))
                .onResponse((req, res) -> {
                })
                .exchangeForResponse();
        assertEquals(401, response.getStatusCode());
        assertEquals("401", response.getBody());
        assertEquals(2, retries.get());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void defaultResponseConsumerHandlesNonStandardStatus(HttpExchanger exchanger) {
        HttpService service = service(exchanger);
        RuntimeException e = assertThrows(RuntimeException.class, () -> service.get("/status/599").exchange());
        assertTrue(e.getMessage().contains("599"), e.getMessage());
        assertEquals("299", service.get("/status/299").exchange());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allExchangers")
    void largeResponseIsAccepted(HttpExchanger exchanger) {
        assertEquals(300 * 1024, service(exchanger).get("/big").exchange().length());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allExchangers")
    void redirectIsFollowedConsistently(HttpExchanger exchanger) {
        HttpResponseEntity<String> response = service(exchanger).get("/redirect").exchangeForResponse();
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().startsWith("METHOD GET\nURI /echo\n"), response.getBody());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exchangers")
    void headIsSupported(HttpExchanger exchanger) {
        HttpResponseEntity<String> response = service(exchanger)
                .method(HttpService.HttpMethod.HEAD).path("/echo").exchangeForResponse();
        assertEquals(200, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void exchangeFailsLoudlyOnConversionError() {
        HttpService service = service(RestClientExchanger.INSTANCE);
        assertThrows(RuntimeException.class, () -> service.get("/html").exchange(Dto.class));
        HttpResponseEntity<Dto> response = service.get("/html").exchangeForResponse(Dto.class);
        assertNull(response.getBody());
        assertNotNull(response.getConversionError());
        assertEquals("<html>oops</html>", response.getRawBody());
    }

    @Test
    void localDateTimeAcceptsIsoAndDefaultPattern() {
        Dto dto = service(RestClientExchanger.INSTANCE).get("/dto").exchange(Dto.class);
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.time);
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0, 0, 123_000_000), dto.time2);
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.time3);
    }

    @Test
    void defaultsAreNotSentToOtherOrigins() {
        HttpService service = HttpService.builder().baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer secret")
                .defaultQueryParam("tk", "1")
                .build();
        String same = service.get("/echo").exchange();
        assertTrue(same.contains("AUTH Bearer secret"), same);
        assertTrue(same.contains("?tk=1"), same);
        String sameAbsolute = service.get(baseUrl + "/echo").exchange();
        assertTrue(sameAbsolute.contains("AUTH Bearer secret"), sameAbsolute);

        String other = service.get(otherOriginUrl + "/echo").addQueryParam("q", "1").exchange();
        assertTrue(other.contains("AUTH null"), other);
        assertTrue(other.contains("URI /echo?q=1\n"), other);
        String explicit = service.get(otherOriginUrl + "/echo")
                .setHeader("Authorization", "Bearer other").exchange();
        assertTrue(explicit.contains("AUTH Bearer other"), explicit);
    }

    @Test
    void debugLogsAtInfoAndMasksCredentials() {
        Logger logger = (Logger) LoggerFactory.getLogger(HttpRequest.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        try {
            service(RestClientExchanger.INSTANCE).get("/echo")
                    .debug(true)
                    .exchange();
        } finally {
            logger.detachAppender(appender);
        }
        List<String> requestLogs = appender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .filter(m -> m.contains("HTTP Request Info"))
                .collect(Collectors.toList());
        assertEquals(1, requestLogs.size());
        assertFalse(requestLogs.get(0).contains("secret"), requestLogs.get(0));
    }

    public static class Dto {
        public String name;
        public LocalDateTime time;
        public LocalDateTime time2;
        public LocalDateTime time3;
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getRawPath();
        byte[] requestBody = exchange.getRequestBody().readAllBytes();
        if (path.startsWith("/status/")) {
            String code = path.substring("/status/".length());
            send(exchange, Integer.parseInt(code), "text/plain", code);
            return;
        }
        switch (path) {
            case "/big":
                send(exchange, 200, "text/plain", "x".repeat(300 * 1024));
                return;
            case "/html":
                send(exchange, 200, "text/html", "<html>oops</html>");
                return;
            case "/dto":
                send(exchange, 200, "application/json", "{\"name\":\"a\",\"time\":\"2024-01-01T10:00:00\","
                        + "\"time2\":\"2024-01-01T10:00:00.123\",\"time3\":\"2024-01-01 10:00:00\"}");
                return;
            case "/redirect":
                exchange.getResponseHeaders().add("Location", "/echo");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            default:
                String rawQuery = exchange.getRequestURI().getRawQuery();
                String echo = "METHOD " + exchange.getRequestMethod()
                        + "\nURI " + path + (rawQuery == null ? "" : "?" + rawQuery)
                        + "\nCT " + exchange.getRequestHeaders().getFirst("Content-Type")
                        + "\nAUTH " + exchange.getRequestHeaders().getFirst("Authorization")
                        + "\n\n" + new String(requestBody, UTF_8);
                send(exchange, 200, "text/plain", echo);
        }
    }

    private static void send(HttpExchange exchange, int code, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType + ";charset=UTF-8");
        if ("HEAD".equals(exchange.getRequestMethod()) || bytes.length == 0) {
            exchange.sendResponseHeaders(code, -1);
        } else {
            exchange.sendResponseHeaders(code, bytes.length);
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }
}
