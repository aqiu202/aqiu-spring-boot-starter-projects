package com.github.aqiu202.http;

import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.exchange.ApacheHttpClientExchanger;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.util.JacksonSerializer;
import com.github.aqiu202.http.util.ParameterizedTypeRef;
import com.github.aqiu202.http.util.RequestDescriptor;
import com.github.aqiu202.http.util.URIBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpUtilsTest {

    @Test
    void fileParamCachesAndClosesOriginalStream() throws IOException {
        AtomicBoolean closed = new AtomicBoolean();
        InputStream input = new ByteArrayInputStream("hello world!".getBytes(UTF_8)) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        };
        FileParam file = FileParam.of(input, "text/plain", "a.txt");
        assertEquals(12, file.getSize());
        assertTrue(closed.get());
        assertEquals("hello world!", new String(readAll(file.getInputStream()), UTF_8));
        InputStream first = file.getInputStream();
        InputStream second = file.getInputStream();
        assertNotSame(first, second);
        assertEquals("hello world!", new String(readAll(second), UTF_8));
        assertEquals("a.txt", file.getFilename());
        assertEquals("text/plain", file.getContentType());
    }

    @Test
    void baseUrlQueryAndTrailingSlashArePreserved() {
        String url = new URIBuilder().baseUrl("http://h/api/?key=1")
                .path("/users/")
                .addQueryParam("a", "b")
                .toUrlString();
        assertEquals("http://h/api/users/?key=1&a=b", url);
    }

    @Test
    void headersAddAllAppendsWithoutSharingLists() {
        HttpHeaders target = new HttpHeaders();
        target.add("X-Id", "1");
        HttpHeaders source = new HttpHeaders();
        source.add("x-id", "2");
        target.addAll(source);
        assertEquals(Arrays.asList("1", "2"), target.get("X-Id"));
        target.add("X-Id", "3");
        assertEquals(Collections.singletonList("2"), source.get("X-Id"));
    }

    @Test
    void headersSkipNullValuesAndMaskAllCredentials() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("a", (Object) null);
        headers.set("b", "1", null);
        assertFalse(headers.contains("a"));
        assertEquals(Collections.singletonList("1"), headers.get("b"));
        headers.add("Authorization", "Bearer auth-secret");
        headers.add("Proxy-Authorization", "Basic proxy-secret");
        headers.add("Cookie", "sid=cookie-secret");
        headers.add("Set-Cookie", "sid=set-cookie-secret");
        headers.add("Accept", "text/plain");
        String text = headers.toString();
        assertFalse(text.contains("auth-secret"), text);
        assertFalse(text.contains("proxy-secret"), text);
        assertFalse(text.contains("cookie-secret"), text);
        assertTrue(text.contains("accept=[text/plain]"), text);
    }

    @Test
    void jackson2SupportsDatesGenericTypesAndStringPassThrough() {
        JacksonSerializer serializer = JacksonSerializer.INSTANCE;
        assertNull(serializer.serialize(null));
        assertEquals("plain", serializer.serialize("plain"));
        assertNull(serializer.deserialize(null, String.class));
        assertNull(serializer.deserialize("", String.class));
        assertEquals("plain", serializer.deserialize("\"plain\"", String.class));

        Map<String, Object> dates = new LinkedHashMap<String, Object>();
        dates.put("date", LocalDate.of(2024, 1, 2));
        dates.put("time", LocalTime.of(3, 4, 5));
        dates.put("dateTime", LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        assertEquals("{\"date\":\"2024-01-02\",\"time\":\"03:04:05\","
                + "\"dateTime\":\"2024-01-02 03:04:05\"}", serializer.serialize(dates));

        List<HttpServiceContract.Dto> result = serializer.deserialize("[{\"name\":\"a\"}]",
                new ParameterizedTypeRef<List<HttpServiceContract.Dto>>() {
                }.getType());
        assertEquals("a", result.get(0).name);
        assertSame(com.fasterxml.jackson.databind.ObjectMapper.class,
                serializer.getObjectMapper().getClass().getSuperclass());
        assertThrows(NullPointerException.class, () -> new JacksonSerializer(null));
        assertThrows(RuntimeException.class, () -> serializer.deserialize("{", Map.class));
    }

    @Test
    void frameworkNeutralTypesAndDefaultExchangerAreLoadable() throws Exception {
        assertSame(ApacheHttpClientExchanger.INSTANCE, HttpService.builder().build().getExchanger());
        RequestDescriptor descriptor = new RequestDescriptor();
        descriptor.setStatusPredicate(status -> status == 599);
        assertTrue(descriptor.getStatusPredicate().test(599));
        assertFalse(descriptor.getStatusPredicate().test(200));
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.github.aqiu202.http.exchange.RestClientExchanger"));
        for (Class<?> type : Arrays.asList(HttpService.class, FileParam.class,
                ParameterizedTypeRef.class, RequestDescriptor.class)) {
            assertNotSame(null, type.getName());
        }
        assertThrows(IllegalArgumentException.class, () -> new ParameterizedTypeRef() {
        });
    }

    @Test
    void interceptorListsAreImmutableDefensiveCopies() {
        HttpInterceptor first = (context, request) -> {
        };
        List<HttpInterceptor> source = new ArrayList<HttpInterceptor>();
        source.add(first);
        RequestDescriptor descriptor = new RequestDescriptor();
        descriptor.setInterceptors(source);
        source.clear();
        assertEquals(Collections.singletonList(first), descriptor.getInterceptors());
        assertThrows(UnsupportedOperationException.class,
                () -> descriptor.getInterceptors().add(first));
        assertThrows(NullPointerException.class,
                () -> descriptor.setInterceptors(Collections.singletonList(null)));

        HttpService service = HttpService.builder().addInterceptors(first).build();
        assertEquals(Collections.singletonList(first), service.getInterceptors());
        assertThrows(UnsupportedOperationException.class,
                () -> service.getInterceptors().add(first));
        assertThrows(NullPointerException.class,
                () -> HttpService.builder().addInterceptors(Collections.singletonList(null)).build());
    }

    private static byte[] readAll(InputStream input) throws IOException {
        try (InputStream stream = input) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = stream.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            return output.toByteArray();
        }
    }
}
