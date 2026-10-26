package com.github.aqiu202.http;

import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.util.URIBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {

    @Test
    void fileParamStreamCanBeReadAfterSize() throws IOException {
        FileParam file = FileParam.of(new ByteArrayInputStream("hello world!".getBytes(UTF_8)),
                "text/plain", "a.txt");
        assertEquals(12, file.getSize());
        assertEquals("hello world!", new String(file.getInputStream().readAllBytes(), UTF_8));
        assertEquals("hello world!", new String(file.getInputStream().readAllBytes(), UTF_8));
    }

    @Test
    void fileParamFromResourceKeepsFilename() {
        ByteArrayResource resource = new ByteArrayResource(new byte[]{1, 2}) {
            @Override
            public String getFilename() {
                return "x.bin";
            }
        };
        FileParam file = FileParam.of(resource);
        assertEquals("x.bin", file.getFilename());
        assertEquals(2, file.getSize());
    }

    @Test
    void fileParamFromMultipartFileKeepsContentType() throws IOException {
        FileParam file = FileParam.of(new SimpleMultipartFile("photo.png", "image/png", new byte[]{1, 2, 3}));
        assertEquals("photo.png", file.getFilename());
        assertEquals("image/png", file.getContentType());
        assertEquals(3, file.getSize());
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
        assertEquals(List.of("1", "2"), target.get("X-Id"));
        target.add("X-Id", "3");
        assertEquals(List.of("2"), source.get("X-Id"));
    }

    @Test
    void headersSkipNullValues() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("a", (Object) null);
        headers.set("b", "1", null);
        assertFalse(headers.contains("a"));
        assertEquals(List.of("1"), headers.get("b"));
    }

    @Test
    void headersToStringMasksCredentials() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer secret");
        headers.add("Cookie", "sid=secret");
        headers.add("Accept", "text/plain");
        String text = headers.toString();
        assertFalse(text.contains("secret"), text);
        assertTrue(text.contains("accept=[text/plain]"), text);
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
