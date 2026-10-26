package com.github.aqiu202.http.data;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpHeaders extends ListMap {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT = "Accept";
    /**
     * 打印时需要脱敏的请求头 / 响应头（小写）
     */
    private static final Set<String> SENSITIVE_HEADERS =
            Set.of("proxy-authorization", "cookie", "set-cookie");
    private static final String MASK = "******";

    public HttpHeaders setContentType(String contentType) {
        this.set(HttpHeaders.CONTENT_TYPE, contentType);
        return this;
    }

    public HttpHeaders setContentLength(long contentLength) {
        this.set(CONTENT_LENGTH, String.valueOf(contentLength));
        return this;
    }

    public HttpHeaders setAccept(String accept) {
        this.set(ACCEPT, accept);
        return this;
    }

    public HttpHeaders json() {
        return this.setContentType("application/json");
    }

    public HttpHeaders form() {
        return this.setContentType("application/x-www-form-urlencoded");
    }

    public HttpHeaders xml() {
        return this.setContentType("application/xml");
    }

    public HttpHeaders text() {
        return this.setContentType("text/plain");
    }

    public HttpHeaders multipart() {
        return this.setContentType("multipart/form-data");
    }

    public HttpHeaders octetStream() {
        return this.setContentType("application/octet-stream");
    }

    public HttpHeaders deepClone() {
        HttpHeaders cloned = new HttpHeaders();
        for (Map.Entry<String, List<String>> entry : this.getValues().entrySet()) {
            cloned.getValues().put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return cloned;
    }

    /**
     * 认证信息、Cookie 等敏感头的取值会被脱敏，避免在调试日志中泄露。
     */
    @Override
    public String toString() {
        Map<String, List<String>> masked = new LinkedHashMap<>();
        this.getValues().forEach((key, values) -> masked.put(key,
                SENSITIVE_HEADERS.contains(key) ? List.of(MASK) : values));
        return masked.toString();
    }
}
