package com.github.aqiu202.http;

import com.github.aqiu202.http.exchange.RestTemplateExchanger;
import com.github.aqiu202.http.exchange.StatefulRestClientExchanger;
import com.github.aqiu202.http.exchange.StatefulRestTemplate;
import com.github.aqiu202.http.exchange.StatefulWebClientExchanger;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.file.SpringFileParams;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringAdapterTest {

    @Test
    void springFileInputsRetainMetadataAndStreamOwnership() throws IOException {
        AtomicBoolean closed = new AtomicBoolean();
        InputStream input = new ByteArrayInputStream("stream".getBytes(UTF_8)) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        };
        InputStreamResource resource = new InputStreamResource(input) {
            @Override
            public String getFilename() {
                return "stream.txt";
            }
        };
        FileParam streamFile = SpringFileParams.of(resource);
        assertEquals("stream.txt", streamFile.getFilename());
        assertEquals(6, streamFile.getSize());
        assertTrue(closed.get());
        assertEquals("stream", new String(streamFile.getBytes(), UTF_8));

        ByteArrayResource bytesResource = new ByteArrayResource(new byte[]{1, 2}) {
            @Override
            public String getFilename() {
                return "x.bin";
            }
        };
        FileParam bytesFile = SpringFileParams.of(bytesResource);
        assertEquals("x.bin", bytesFile.getFilename());
        assertEquals(2, bytesFile.getSize());

        FileParam multipart = SpringFileParams.of(new SimpleMultipartFile(
                "photo.png", "image/png", new byte[]{1, 2, 3}));
        assertEquals("photo.png", multipart.getFilename());
        assertEquals("image/png", multipart.getContentType());
        assertEquals(3, multipart.getSize());
    }

    @Test
    void springResourceOutputsRetainFilenameLengthAndContent() throws IOException {
        FileParam file = FileParam.of("resource-content".getBytes(UTF_8), "text/plain", "a.txt");
        ByteArrayResource bytes = SpringFileParams.toByteArrayResource(file);
        assertEquals("a.txt", bytes.getFilename());
        assertEquals("resource-content", new String(bytes.getByteArray(), UTF_8));

        InputStreamResource stream = SpringFileParams.toInputStreamResource(file);
        assertEquals("a.txt", stream.getFilename());
        assertEquals(file.getSize(), stream.contentLength());
        assertEquals("resource-content", new String(stream.getInputStream().readAllBytes(), UTF_8));
        assertEquals("resource-content", new String(file.getInputStream().readAllBytes(), UTF_8));
    }

    @Test
    void synchronousClientsLoadAndRunWithoutWebFlux() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/echo", exchange -> {
            byte[] body = "ok".getBytes(UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        List<URL> urls = new ArrayList<>();
        String[] entries = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String entry : entries) {
            String normalized = entry.replace('\\', '/');
            if (!normalized.contains("/spring-webflux-")
                    && !normalized.contains("/reactor-core-")
                    && !normalized.contains("/reactive-streams-")) {
                urls.add(new File(entry).toURI().toURL());
            }
        }
        try (URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]),
                ClassLoader.getPlatformClassLoader())) {
            assertThrows(ClassNotFoundException.class, () -> Class.forName(
                    "org.springframework.web.reactive.function.client.WebClient", false, loader));
            Class<?> serviceType = loader.loadClass("com.github.aqiu202.http.HttpService");
            Class<?> exchangerType = loader.loadClass("com.github.aqiu202.http.exchange.HttpExchanger");
            String uri = "http://127.0.0.1:" + server.getAddress().getPort() + "/echo";
            for (String className : new String[]{
                    "com.github.aqiu202.http.exchange.RestClientExchanger",
                    "com.github.aqiu202.http.exchange.RestTemplateExchanger"}) {
                Class<?> implementation = loader.loadClass(className);
                Object exchanger = implementation.getMethod("create").invoke(null);
                Object builder = serviceType.getMethod("builder").invoke(null);
                builder = builder.getClass().getMethod("baseUrl", String.class)
                        .invoke(builder, uri);
                builder = builder.getClass().getMethod("exchanger", exchangerType)
                        .invoke(builder, exchanger);
                Object service = builder.getClass().getMethod("build").invoke(builder);
                Object request = serviceType.getMethod("get", String.class, Object[].class)
                        .invoke(service, new Object[]{"", new Object[0]});
                assertEquals("ok", request.getClass().getMethod("exchange").invoke(request), className);
            }
        } finally {
            server.stop(0);
        }
    }

    @Test
    void springStatefulVariantsCanClearSessions() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            if (exchange.getRequestURI().getPath().equals("/login")) {
                exchange.getResponseHeaders().add("Set-Cookie", "sid=secret; Path=/");
            }
            String bodyText = "COOKIE " + exchange.getRequestHeaders().getFirst("Cookie");
            byte[] body = bodyText.getBytes(UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        String baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
        StatefulRestClientExchanger restClient = new StatefulRestClientExchanger();
        RestTemplateExchanger restTemplate = RestTemplateExchanger.stateful();
        StatefulRestTemplate statefulTemplate = (StatefulRestTemplate) restTemplate.getRequestInstance();
        StatefulWebClientExchanger webClient = new StatefulWebClientExchanger();
        List<Object[]> variants = Arrays.asList(
                new Object[]{restClient, (Runnable) restClient::clearCookies},
                new Object[]{restTemplate, (Runnable) statefulTemplate::clearCookies},
                new Object[]{webClient, (Runnable) webClient::clearCookies});
        try {
            for (Object[] variant : variants) {
                HttpService service = HttpService.builder().baseUrl(baseUrl)
                        .exchanger((com.github.aqiu202.http.exchange.HttpExchanger) variant[0]).build();
                service.get("/login").exchange();
                assertEquals("COOKIE sid=secret", service.get("/echo").exchange());
                ((Runnable) variant[1]).run();
                assertEquals("COOKIE null", service.get("/echo").exchange());
            }
        } finally {
            server.stop(0);
        }
    }

    private static final class SimpleMultipartFile implements MultipartFile {
        private final String filename;
        private final String contentType;
        private final byte[] data;

        private SimpleMultipartFile(String filename, String contentType, byte[] data) {
            this.filename = filename;
            this.contentType = contentType;
            this.data = data;
        }

        @Override
        public String getName() {
            return "file";
        }

        @Override
        public String getOriginalFilename() {
            return this.filename;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return this.data.length == 0;
        }

        @Override
        public long getSize() {
            return this.data.length;
        }

        @Override
        public byte[] getBytes() {
            return this.data;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.data);
        }

        @Override
        public void transferTo(File dest) {
            throw new UnsupportedOperationException();
        }
    }
}
