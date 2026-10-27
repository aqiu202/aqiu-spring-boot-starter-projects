package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.HttpServiceContract;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.file.FileParam;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHttpRequest;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApacheHttpClientExchangerTest {

    private static HttpServer server;
    private static String baseUrl;
    private static String otherOriginUrl;
    private static final AtomicInteger SLOW_HITS = new AtomicInteger();
    private static final AtomicInteger CROSS_HITS = new AtomicInteger();

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", ApacheHttpClientExchangerTest::handle);
        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://127.0.0.1:" + port;
        otherOriginUrl = "http://localhost:" + port;
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void durationConfigurationIsValidatedBeforeCreatingClient() {
        assertThrows(NullPointerException.class,
                () -> ApacheHttpClientExchanger.create(null, Duration.ofSeconds(1)));
        assertThrows(NullPointerException.class,
                () -> ApacheHttpClientExchanger.create(Duration.ofSeconds(1), null));
        assertThrows(IllegalArgumentException.class,
                () -> ApacheHttpClientExchanger.create(Duration.ZERO, Duration.ofSeconds(1)));
        assertThrows(IllegalArgumentException.class,
                () -> ApacheHttpClientExchanger.create(Duration.ofNanos(999999), Duration.ofSeconds(1)));
        assertThrows(IllegalArgumentException.class,
                () -> ApacheHttpClientExchanger.create(Duration.ofSeconds(Long.MAX_VALUE), Duration.ofSeconds(1)));
        assertThrows(NullPointerException.class, () -> ApacheHttpClientExchanger.create(null));
    }

    @Test
    void readTimeoutFailsWithoutAutomaticNetworkRetry() throws IOException {
        SLOW_HITS.set(0);
        long start = System.nanoTime();
        try (ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create(
                Duration.ofSeconds(1), Duration.ofMillis(100))) {
            RuntimeException error = assertThrows(RuntimeException.class,
                    () -> service(exchanger).get("/slow").exchange());
            assertNotNull(error.getCause());
            assertTrue(error.getCause() instanceof IOException, error.getCause().toString());
        }
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        assertTrue(elapsedMillis < 3000, "读取超时耗时：" + elapsedMillis);
        assertEquals(1, SLOW_HITS.get());
    }

    @Test
    void singleConnectionIsReleasedAfterEveryResponsePath() throws IOException {
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(1);
        pool.setDefaultMaxPerRoute(1);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(pool)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setResponseTimeout(2, TimeUnit.SECONDS)
                        .setConnectionRequestTimeout(1, TimeUnit.SECONDS)
                        .build())
                .disableAutomaticRetries()
                .disableCookieManagement()
                .build();
        ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create(client);
        try {
            HttpService service = service(exchanger);
            HttpResponseEntity<String> error = service.get("/status/500")
                    .onResponse((request, response) -> {
                    }).exchangeForResponse();
            assertEquals(500, error.getStatusCode());
            HttpResponseEntity<HttpServiceContract.Dto> invalid = service.get("/html")
                    .exchangeForResponse(HttpServiceContract.Dto.class);
            assertNotNull(invalid.getConversionError());
            assertThrows(RuntimeException.class, () -> service.get("/broken").exchange());
            assertTrue(service.get("/echo").exchange().contains("METHOD GET"));
            assertEquals(1, pool.getTotalStats().getMax());
        } finally {
            exchanger.close();
            client.close();
        }
    }

    @Test
    void closeIsIdempotentAndRejectsRequests() throws IOException {
        ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create();
        HttpService service = service(exchanger);
        assertTrue(service.get("/echo").exchange().contains("METHOD GET"));
        exchanger.close();
        exchanger.close();
        assertThrows(IllegalStateException.class, () -> service.get("/echo").exchange());

        ApacheHttpClientExchanger unused = ApacheHttpClientExchanger.create();
        unused.close();
        unused.close();
        assertThrows(IllegalStateException.class, unused::getRequestInstance);
    }

    @Test
    void externalClientOwnershipAndConfigurationArePreserved() throws IOException {
        CloseableHttpClient external = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setRedirectsEnabled(false).build())
                .disableCookieManagement()
                .build();
        ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create(external);
        HttpService service = service(exchanger);
        assertEquals(302, service.get("/redirect/302").onResponse((request, response) -> {
        }).exchangeForResponse().getStatusCode());
        exchanger.close();
        exchanger.close();
        assertThrows(IllegalStateException.class, () -> service.get("/echo").exchange());
        int status = external.execute(new HttpGet(baseUrl + "/echo"), response -> {
            EntityUtils.consume(response.getEntity());
            return response.getCode();
        });
        assertEquals(200, status);
        external.close();
    }

    @Test
    void sharedInstanceCannotBeClosedByOneCaller() throws IOException {
        assertThrows(IllegalStateException.class, ApacheHttpClientExchanger.INSTANCE::close);
        assertTrue(HttpService.builder().baseUrl(baseUrl).build().get("/echo")
                .exchange().contains("METHOD GET"));
    }

    @Test
    void redirectsConvertMethodsAndReplayBodies() throws IOException {
        try (ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create()) {
            HttpService service = service(exchanger);
            for (int status : new int[]{301, 302}) {
                String echo = service.post("/redirect/" + status).body("payload").exchange();
                assertTrue(echo.startsWith("METHOD GET\n"), echo);
                assertTrue(echo.endsWith("\n\n"), echo);
            }
            String seeOther = service.put("/redirect/303").body("payload").exchange();
            assertTrue(seeOther.startsWith("METHOD GET\n"), seeOther);
            assertTrue(seeOther.endsWith("\n\n"), seeOther);

            for (int status : new int[]{307, 308}) {
                FileParam file = FileParam.of("complete-file".getBytes(UTF_8), "text/plain", "a.txt");
                HttpBodyRequest request = service.post("/redirect/" + status)
                        .body(new HttpBodyEntity().addParameters("file", file));
                String echo = request.exchange();
                assertTrue(echo.startsWith("METHOD POST\n"), echo);
                assertTrue(echo.contains("complete-file"), echo);
                assertTrue(echo.contains("filename=\"a.txt\""), echo);
            }
        }
    }

    @Test
    void crossOriginRedirectsNeverForwardExplicitCredentials() throws IOException {
        try (ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create()) {
            HttpService service = service(exchanger);
            String[] names = {"Authorization", "Proxy-Authorization", "Cookie"};
            for (String name : names) {
                CROSS_HITS.set(0);
                HttpResponseEntity<String> response = service.get("/cross-redirect")
                        .setHeader(name, "secret").onResponse((request, result) -> {
                        }).exchangeForResponse();
                assertEquals(302, response.getStatusCode(), name);
                assertNull(response.getBody());
                assertEquals(0, CROSS_HITS.get(), name);
            }
            CROSS_HITS.set(0);
            assertEquals(200, service.get("/cross-redirect").exchangeForResponse().getStatusCode());
            assertEquals(1, CROSS_HITS.get());
        }
    }

    @Test
    void redirectLimitAndCircularRedirectsTerminate() throws IOException {
        try (ApacheHttpClientExchanger exchanger = ApacheHttpClientExchanger.create()) {
            HttpService service = service(exchanger);
            assertEquals(200, service.get("/chain/0").exchangeForResponse().getStatusCode());
            assertThrows(RuntimeException.class, () -> service.get("/overflow").exchange());
            assertThrows(RuntimeException.class, () -> service.get("/loop-a").exchange());
            assertTrue(service.get("/echo").exchange().contains("METHOD GET"));
        }
    }

    @Test
    void redirectStrategyRejectsDowngradeAndProxyCredentials() {
        ApacheHttpClientExchanger.SafeRedirectStrategy strategy =
                new ApacheHttpClientExchanger.SafeRedirectStrategy();
        BasicHttpRequest request = new BasicHttpRequest("GET", "/");
        BasicHttpContext context = new BasicHttpContext();
        assertFalse(strategy.isRedirectAllowed(new HttpHost("https", "example.com", 443),
                new HttpHost("http", "example.com", 80), request, context));
        request.addHeader("Proxy-Authorization", "Basic secret");
        assertFalse(strategy.isRedirectAllowed(new HttpHost("http", "example.com", 80),
                new HttpHost("http", "other.example.com", 80), request, context));
    }

    @Test
    void statefulCookiesAreIsolatedClearableAndMatchRules() throws IOException {
        try (StatefulApacheHttpClientExchanger first = ApacheHttpClientExchanger.stateful();
             StatefulApacheHttpClientExchanger second = ApacheHttpClientExchanger.stateful();
             ApacheHttpClientExchanger stateless = ApacheHttpClientExchanger.create()) {
            service(first).get("/cookie-login").exchange();
            assertTrue(service(first).get("/echo").exchange().contains("COOKIE sid=secret"));
            assertTrue(service(second).get("/echo").exchange().contains("COOKIE null"));
            assertTrue(service(stateless).get("/echo").exchange().contains("COOKIE null"));
            first.clearCookies();
            assertTrue(first.getCookies().isEmpty());
            assertTrue(service(first).get("/echo").exchange().contains("COOKIE null"));

            service(first).get("/cookie-rules").exchange();
            String rules = service(first).get("/cookie/echo").exchange();
            assertTrue(rules.contains("visible=yes"), rules);
            assertFalse(rules.contains("hidden=no"), rules);
            assertFalse(rules.contains("secure=no"), rules);
            assertFalse(rules.contains("domain=no"), rules);
            List<String> names = first.getCookies().stream().map(Cookie::getName).collect(Collectors.toList());
            assertTrue(names.contains("visible"), names.toString());
            assertFalse(names.contains("domain"), names.toString());

            BasicClientCookie manual = new BasicClientCookie("manual", "yes");
            manual.setDomain("127.0.0.1");
            manual.setPath("/");
            first.addCookie(manual);
            assertTrue(service(first).get("/echo").exchange().contains("manual=yes"));
            assertFalse(first.getCookieStore() == second.getCookieStore());
        }
    }

    private static HttpService service(HttpExchanger exchanger) {
        return HttpService.builder().baseUrl(baseUrl).exchanger(exchanger).build();
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getRawPath();
        byte[] body = readAll(exchange.getRequestBody());
        if (path.startsWith("/redirect/")) {
            exchange.getResponseHeaders().add("Location", "/echo");
            exchange.sendResponseHeaders(Integer.parseInt(path.substring("/redirect/".length())), -1);
            exchange.close();
            return;
        }
        if (path.startsWith("/chain/")) {
            int step = Integer.parseInt(path.substring("/chain/".length()));
            if (step >= 10) {
                send(exchange, 200, "ok".getBytes(UTF_8));
            } else {
                exchange.getResponseHeaders().add("Location", "/chain/" + (step + 1));
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
            }
            return;
        }
        switch (path) {
            case "/slow":
                SLOW_HITS.incrementAndGet();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                send(exchange, 200, "slow".getBytes(UTF_8));
                return;
            case "/broken":
                exchange.sendResponseHeaders(200, 100);
                exchange.getResponseBody().write(new byte[]{1, 2, 3});
                exchange.close();
                return;
            case "/html":
                send(exchange, 200, "<html>oops</html>".getBytes(UTF_8));
                return;
            case "/cross-redirect":
                exchange.getResponseHeaders().add("Location", otherOriginUrl + "/echo-cross");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            case "/echo-cross":
                CROSS_HITS.incrementAndGet();
                send(exchange, 200, exchange.getRequestHeaders().getFirst("Authorization") == null
                        ? "cross".getBytes(UTF_8) : "leaked".getBytes(UTF_8));
                return;
            case "/overflow":
                exchange.getResponseHeaders().add("Location", "/chain/0");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            case "/loop-a":
                exchange.getResponseHeaders().add("Location", "/loop-b");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            case "/loop-b":
                exchange.getResponseHeaders().add("Location", "/loop-a");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
                return;
            case "/cookie-login":
                exchange.getResponseHeaders().add("Set-Cookie", "sid=secret; Path=/");
                send(exchange, 200, "ok".getBytes(UTF_8));
                return;
            case "/cookie-rules":
                exchange.getResponseHeaders().add("Set-Cookie", "visible=yes; Path=/cookie");
                exchange.getResponseHeaders().add("Set-Cookie", "hidden=no; Path=/other");
                exchange.getResponseHeaders().add("Set-Cookie", "secure=no; Secure; Path=/");
                exchange.getResponseHeaders().add("Set-Cookie", "domain=no; Domain=example.com; Path=/");
                send(exchange, 200, "ok".getBytes(UTF_8));
                return;
            default:
                if (path.startsWith("/status/")) {
                    String status = path.substring("/status/".length());
                    send(exchange, Integer.parseInt(status), status.getBytes(UTF_8));
                    return;
                }
                String echo = "METHOD " + exchange.getRequestMethod()
                        + "\nCOOKIE " + exchange.getRequestHeaders().getFirst("Cookie")
                        + "\n\n" + new String(body, UTF_8);
                exchange.getResponseHeaders().add("X-Method", exchange.getRequestMethod());
                exchange.getResponseHeaders().add("X-Body-Length", String.valueOf(body.length));
                send(exchange, 200, echo.getBytes(UTF_8));
        }
    }

    private static void send(HttpExchange exchange, int status, byte[] body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        if (body.length == 0) {
            exchange.sendResponseHeaders(status, -1);
        } else {
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
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
}
