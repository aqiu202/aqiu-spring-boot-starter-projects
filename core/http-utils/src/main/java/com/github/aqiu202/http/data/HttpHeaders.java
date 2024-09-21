package com.github.aqiu202.http.data;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpHeaders extends ListMap {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT = "Accept";

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
}
