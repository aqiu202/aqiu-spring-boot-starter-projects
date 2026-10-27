package com.github.aqiu202.http;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.ApacheHttpClientExchanger;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.util.ParameterizedTypeRef;
import com.github.aqiu202.http.util.RetryPolicy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 各 HTTP 实现共同遵守的 Java 8 端到端契约，由核心与 Spring 测试模块复用。
 */
public abstract class HttpServiceContract {

    private static HttpServer server;
    private static String baseUrl;
    private static String otherOriginUrl;

    @BeforeAll
    protected static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", HttpServiceContract::handle);
        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://127.0.0.1:" + port;
        otherOriginUrl = "http://localhost:" + port;
    }

    @AfterAll
    protected static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    protected abstract Stream<Named<HttpExchanger>> exchangers();

    protected Stream<Named<HttpExchanger>> statefulExchangers() {
        return Stream.empty();
    }

    protected final String baseUrl() {
        return baseUrl;
    }

    private Stream<Named<HttpExchanger>> allExchangers() {
        return Stream.concat(this.exchangers(), this.statefulExchangers());
    }

    private static HttpService service(HttpExchanger exchanger) {
        return HttpService.builder().baseUrl(baseUrl).exchanger(exchanger).build();
    }

    @TestFactory
    public Stream<DynamicTest> methodsUrisAndHeaders() {
        return this.tests("方法、URI 与多值头", false, exchanger -> {
            for (HttpService.HttpMethod method : HttpService.HttpMethod.values()) {
                HttpResponseEntity<String> response = service(exchanger).method(method).path("/echo")
                        .addHeader("X-Multi", "one", "two")
                        .body("ignored")
                        .exchangeForResponse();
                assertEquals(method.getName(), response.getHeaders().getFirst("X-Method"));
                boolean supportsBody = method == HttpService.HttpMethod.POST
                        || method == HttpService.HttpMethod.PUT
                        || method == HttpService.HttpMethod.PATCH
                        || method == HttpService.HttpMethod.DELETE;
                assertEquals(supportsBody ? "7" : "0", response.getHeaders().getFirst("X-Body-Length"));
                assertEquals(Arrays.asList("one", "two"), response.getHeaders().get("X-Received-Multi"));
            }
            String echo = service(exchanger).get("/echo/{id}/{name}", "a&b/c d", "中")
                    .addQueryParam("a", "1", null, "2")
                    .addQueryParam("status", (Object) null)
                    .exchange();
            assertTrue(echo.contains("URI /echo/a%26b%2Fc%20d/%E4%B8%AD?a=1&a=2\n"), echo);
        });
    }

    @TestFactory
    public Stream<DynamicTest> requestBodies() {
        return this.tests("原始、文件与 JSON 请求体", false, exchanger -> {
            HttpService service = service(exchanger);
            HttpResponseEntity<String> text = service.post("/echo")
                    .contentType("text/plain;charset=GBK").body("中文").exchangeForResponse();
            assertEquals(Base64.getEncoder().encodeToString("中文".getBytes(Charset.forName("GBK"))),
                    text.getHeaders().getFirst("X-Body-Base64"));

            byte[] bytes = new byte[]{0, 1, 2, 127};
            HttpResponseEntity<String> binary = service.post("/echo").body(bytes).exchangeForResponse();
            assertEquals(Base64.getEncoder().encodeToString(bytes), binary.getHeaders().getFirst("X-Body-Base64"));

            FileParam file = FileParam.of(new ByteArrayInputStream("file-body".getBytes(UTF_8)),
                    "text/plain", "a.txt");
            HttpResponseEntity<String> rawFile = service.post("/echo")
                    .contentType(file.getContentType()).body(file).exchangeForResponse();
            assertEquals(Base64.getEncoder().encodeToString("file-body".getBytes(UTF_8)),
                    rawFile.getHeaders().getFirst("X-Body-Base64"));

            Dto dto = new Dto();
            dto.name = "jackson";
            String json = service.post("/echo").body(dto).exchange();
            assertTrue(json.contains("CT application/json"), json);
            assertTrue(json.contains("\"name\":\"jackson\""), json);
            assertEquals("0", service.post("/echo").exchangeForResponse()
                    .getHeaders().getFirst("X-Body-Length"));
        });
    }

    @TestFactory
    public Stream<DynamicTest> formsAndMultipartReplay() {
        return this.tests("表单与 multipart 重放", false, exchanger -> {
            HttpService service = service(exchanger);
            HttpBodyRequest form = service.post("/echo");
            form.getHeaders().form();
            String formEcho = form.body(new HttpBodyEntity()
                            .addParameters("a", 1)
                            .addParameters("b", "x y", "中"))
                    .exchange();
            assertTrue(formEcho.endsWith("\n\na=1&b=x+y&b=%E4%B8%AD"), formEcho);

            FileParam file = FileParam.of(new ByteArrayInputStream("hello world!".getBytes(UTF_8)),
                    "image/png", "a.bin");
            HttpBodyRequest multipart = service.post("/echo")
                    .body(new HttpBodyEntity().addParameters("file", file).addParameters("name", "n1"));
            assertNotNull(multipart.toString());
            String multipartEcho = multipart.exchange();
            assertTrue(multipartEcho.contains("hello world!"), multipartEcho);
            assertTrue(multipartEcho.contains("filename=\"a.bin\""), multipartEcho);
            assertTrue(multipartEcho.contains("Content-Type: image/png"), multipartEcho);

            AtomicInteger attempts = new AtomicInteger();
            FileParam retryFile = FileParam.of(new ByteArrayInputStream("retry-body".getBytes(UTF_8)),
                    "text/plain", "retry.txt");
            HttpResponseEntity<String> response = service.post("/echo")
                    .body(new HttpBodyEntity().addParameters("file", retryFile))
                    .retry(RetryPolicy.of(r -> attempts.incrementAndGet() == 1, 1))
                    .exchangeForResponse();
            assertEquals(2, attempts.get());
            assertTrue(response.getBody().contains("retry-body"), response.getBody());

            HttpBodyRequest invalidForm = service.post("/echo");
            invalidForm.getHeaders().form();
            invalidForm.body(new HttpBodyEntity().addParameters("f",
                    FileParam.of(new byte[]{1}, "text/plain", "a.txt")));
            assertThrows(IllegalArgumentException.class, invalidForm::exchange);
            assertThrows(IllegalArgumentException.class, () -> new HttpBodyEntity()
                    .addParameters("f", FileParam.of(new byte[]{1}, "text/plain", " ")));
        });
    }

    @TestFactory
    public Stream<DynamicTest> statusesRetriesAndConversion() {
        return this.tests("状态、重试与转换失败", false, exchanger -> {
            HttpService service = service(exchanger);
            assertEquals("299", service.get("/status/299").exchange());
            RuntimeException error = assertThrows(RuntimeException.class,
                    () -> service.get("/status/599").exchange());
            assertTrue(error.getMessage().contains("599"), error.getMessage());

            AtomicInteger retries = new AtomicInteger();
            AtomicInteger consumers = new AtomicInteger();
            HttpResponseEntity<String> response = service.get("/status/401")
                    .retry(RetryPolicy.of(r -> r.getStatusCode() == 401, 2,
                            r -> retries.incrementAndGet()))
                    .onResponse((request, result) -> consumers.incrementAndGet())
                    .exchangeForResponse();
            assertEquals(401, response.getStatusCode());
            assertEquals("401", response.getBody());
            assertEquals(2, retries.get());
            assertEquals(1, consumers.get());

            IllegalStateException conversion = assertThrows(IllegalStateException.class,
                    () -> service.get("/html").exchange(Dto.class));
            assertTrue(conversion.getMessage().contains("200"), conversion.getMessage());
            HttpResponseEntity<Dto> invalid = service.get("/html").exchangeForResponse(Dto.class);
            assertNull(invalid.getBody());
            assertNotNull(invalid.getConversionError());
            assertEquals("<html>oops</html>", invalid.getRawBody());
        });
    }

    @TestFactory
    public Stream<DynamicTest> emptyCharsetBytesAndDates() {
        return this.tests("空响应、字符集、字节、泛型与日期", false, exchanger -> {
            HttpService service = service(exchanger);
            HttpResponseEntity<String> empty = service.get("/empty").exchangeForResponse();
            assertNull(empty.getBody());
            assertNull(empty.getRawBody());
            assertNull(empty.getConversionError());
            HttpResponseEntity<String> noContent = service.get("/status/204").exchangeForResponse();
            assertNull(noContent.getBody());
            assertNull(noContent.getRawBody());

            assertEquals("中文", service.get("/charset/gbk").exchange());
            assertEquals("中文", service.get("/charset/invalid").exchange());
            HttpResponseEntity<byte[]> binary = service.get("/bytes").exchangeForResponse(byte[].class);
            assertArrayEquals(new byte[]{0, 1, 2, -1}, binary.getBody());

            Dto dto = service.get("/dto-gbk").exchange(Dto.class);
            assertEquals("中文", dto.name);
            assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.time);
            assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0, 0, 123000000), dto.time2);
            assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.time3);
            List<Dto> list = service.get("/dto-list").exchange(new ParameterizedTypeRef<List<Dto>>() {
            });
            assertEquals(2, list.size());
            assertEquals("b", list.get(1).name);
        });
    }

    @TestFactory
    public Stream<DynamicTest> redirectsLargeResponsesAndDefaults() {
        return this.tests("同源跳转、大响应与默认凭证隔离", true, exchanger -> {
            HttpService service = service(exchanger);
            HttpResponseEntity<String> redirect = service.get("/redirect").exchangeForResponse();
            assertEquals(200, redirect.getStatusCode());
            assertTrue(redirect.getBody().startsWith("METHOD GET\nURI /echo\n"), redirect.getBody());
            assertEquals(300 * 1024, service.get("/big").exchange().length());

            HttpService secured = HttpService.builder().baseUrl(baseUrl)
                    .exchanger(exchanger)
                    .defaultHeader("Authorization", "Bearer secret")
                    .defaultQueryParam("tk", "1")
                    .build();
            assertTrue(secured.get("/echo").exchange().contains("AUTH Bearer secret"));
            String other = secured.get(otherOriginUrl + "/echo").addQueryParam("q", "1").exchange();
            assertTrue(other.contains("AUTH null"), other);
            assertTrue(other.contains("URI /echo?q=1\n"), other);
            String explicit = secured.get(otherOriginUrl + "/echo")
                    .setHeader("Authorization", "Bearer other").exchange();
            assertTrue(explicit.contains("AUTH Bearer other"), explicit);
        });
    }

    @TestFactory
    public Stream<DynamicTest> cookiePersistenceContract() {
        Stream<DynamicTest> stateless = this.tests("普通实例不持久化 Cookie", false, exchanger -> {
            HttpService service = service(exchanger);
            service.get("/cookie-login").exchange();
            assertTrue(service.get("/echo").exchange().contains("COOKIE null"));
        });
        Stream<DynamicTest> stateful = this.statefulTests("有状态实例保留 Cookie", exchanger -> {
            HttpService service = service(exchanger);
            service.get("/cookie-login").exchange();
            assertTrue(service.get("/echo").exchange().contains("COOKIE sid=secret"));
        });
        return Stream.concat(stateless, stateful);
    }

    @TestFactory
    public Stream<DynamicTest> loggingAndConcurrentInterceptors() {
        return this.tests("日志脱敏与并发拦截器隔离", false, exchanger -> {
            Logger logger = (Logger) LoggerFactory.getLogger(HttpRequest.class);
            ListAppender<ILoggingEvent> appender = new ListAppender<ILoggingEvent>();
            appender.start();
            logger.addAppender(appender);
            try {
                service(exchanger).get("/echo")
                        .setHeader("Authorization", "Bearer auth-secret")
                        .setHeader("Cookie", "sid=cookie-secret")
                        .setHeader("Proxy-Authorization", "Basic proxy-secret")
                        .debug(true)
                        .exchange();
            } finally {
                logger.detachAppender(appender);
            }
            List<String> logs = appender.list.stream()
                    .map(ILoggingEvent::getFormattedMessage)
                    .filter(message -> message.contains("HTTP Request Info"))
                    .collect(Collectors.toList());
            assertEquals(1, logs.size());
            assertFalse(logs.get(0).contains("auth-secret"), logs.get(0));
            assertFalse(logs.get(0).contains("cookie-secret"), logs.get(0));
            assertFalse(logs.get(0).contains("proxy-secret"), logs.get(0));

            HttpInterceptor interceptor = (context, request) -> request.addHeader("X-Service", "yes");
            HttpService service = HttpService.builder().baseUrl(baseUrl).exchanger(exchanger)
                    .addInterceptors(interceptor).build();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            try {
                List<Callable<String>> tasks = new ArrayList<Callable<String>>();
                for (int i = 0; i < 8; i++) {
                    final String id = String.valueOf(i);
                    tasks.add(() -> service.get("/echo").configure(request ->
                            request.addHeader("X-Request", id)).exchange());
                }
                List<Future<String>> results = executor.invokeAll(tasks);
                for (int i = 0; i < results.size(); i++) {
                    String result = results.get(i).get();
                    assertTrue(result.contains("REQUEST " + i), result);
                    assertTrue(result.contains("SERVICE yes"), result);
                }
            } finally {
                executor.shutdownNow();
            }
            assertEquals(Collections.singletonList(interceptor), service.getInterceptors());
        });
    }

    private Stream<DynamicTest> tests(String name, boolean includeStateful,
                                      ThrowingConsumer<HttpExchanger> test) {
        Stream<Named<HttpExchanger>> source = includeStateful ? this.allExchangers() : this.exchangers();
        return source.map(named -> DynamicTest.dynamicTest(name + " - " + named.getName(), () -> {
            HttpExchanger exchanger = named.getPayload();
            try {
                test.accept(exchanger);
            } finally {
                closeIfOwned(exchanger);
            }
        }));
    }

    private Stream<DynamicTest> statefulTests(String name, ThrowingConsumer<HttpExchanger> test) {
        return this.statefulExchangers().map(named -> DynamicTest.dynamicTest(
                name + " - " + named.getName(), () -> {
                    HttpExchanger exchanger = named.getPayload();
                    try {
                        test.accept(exchanger);
                    } finally {
                        closeIfOwned(exchanger);
                    }
                }));
    }

    private static void closeIfOwned(HttpExchanger exchanger) throws IOException {
        if (exchanger instanceof Closeable && exchanger != ApacheHttpClientExchanger.INSTANCE) {
            ((Closeable) exchanger).close();
        }
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getRawPath();
        byte[] requestBody = readAll(exchange.getRequestBody());
        if (path.startsWith("/status/")) {
            String code = path.substring("/status/".length());
            send(exchange, Integer.parseInt(code), "text/plain;charset=UTF-8",
                    code.getBytes(UTF_8));
            return;
        }
        if (path.startsWith("/charset/")) {
            if (path.endsWith("gbk")) {
                send(exchange, 200, "text/plain;charset=GBK", "中文".getBytes(Charset.forName("GBK")));
            } else {
                send(exchange, 200, "text/plain;charset=not-a-charset", "中文".getBytes(UTF_8));
            }
            return;
        }
        switch (path) {
            case "/big":
                char[] chars = new char[300 * 1024];
                Arrays.fill(chars, 'x');
                send(exchange, 200, "text/plain;charset=UTF-8", new String(chars).getBytes(UTF_8));
                return;
            case "/html":
                send(exchange, 200, "text/html;charset=UTF-8", "<html>oops</html>".getBytes(UTF_8));
                return;
            case "/dto-gbk":
                String json = "{\"name\":\"中文\",\"time\":\"2024-01-01T10:00:00\","
                        + "\"time2\":\"2024-01-01T10:00:00.123\",\"time3\":\"2024-01-01 10:00:00\"}";
                send(exchange, 200, "application/json;charset=GBK", json.getBytes(Charset.forName("GBK")));
                return;
            case "/dto-list":
                send(exchange, 200, "application/json;charset=UTF-8",
                        "[{\"name\":\"a\"},{\"name\":\"b\"}]".getBytes(UTF_8));
                return;
            case "/bytes":
                send(exchange, 200, "application/octet-stream", new byte[]{0, 1, 2, -1});
                return;
            case "/empty":
                send(exchange, 200, "text/plain;charset=UTF-8", new byte[0]);
                return;
            case "/redirect":
                exchange.getResponseHeaders().add("Location", "/echo");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            case "/cookie-login":
                exchange.getResponseHeaders().add("Set-Cookie", "sid=secret; Path=/");
                send(exchange, 200, "text/plain;charset=UTF-8", "ok".getBytes(UTF_8));
                return;
            default:
                String rawQuery = exchange.getRequestURI().getRawQuery();
                String echo = "METHOD " + exchange.getRequestMethod()
                        + "\nURI " + path + (rawQuery == null ? "" : "?" + rawQuery)
                        + "\nCT " + exchange.getRequestHeaders().getFirst("Content-Type")
                        + "\nAUTH " + exchange.getRequestHeaders().getFirst("Authorization")
                        + "\nCOOKIE " + exchange.getRequestHeaders().getFirst("Cookie")
                        + "\nREQUEST " + exchange.getRequestHeaders().getFirst("X-Request")
                        + "\nSERVICE " + exchange.getRequestHeaders().getFirst("X-Service")
                        + "\n\n" + new String(requestBody, UTF_8);
                exchange.getResponseHeaders().add("X-Method", exchange.getRequestMethod());
                exchange.getResponseHeaders().add("X-Body-Length", String.valueOf(requestBody.length));
                exchange.getResponseHeaders().add("X-Body-Base64", Base64.getEncoder().encodeToString(requestBody));
                List<String> multi = exchange.getRequestHeaders().get("X-Multi");
                if (multi != null) {
                    for (String value : multi) {
                        exchange.getResponseHeaders().add("X-Received-Multi", value);
                    }
                }
                send(exchange, 200, "text/plain;charset=UTF-8", echo.getBytes(UTF_8));
        }
    }

    private static void send(HttpExchange exchange, int code, String contentType, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", contentType);
        if ("HEAD".equals(exchange.getRequestMethod()) || code == 204 || bytes.length == 0) {
            exchange.sendResponseHeaders(code, -1);
        } else {
            exchange.sendResponseHeaders(code, bytes.length);
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }

    private static byte[] readAll(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
        return output.toByteArray();
    }

    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T value) throws Exception;
    }

    public static class Dto {
        public String name;
        public LocalDateTime time;
        public LocalDateTime time2;
        public LocalDateTime time3;
    }
}
