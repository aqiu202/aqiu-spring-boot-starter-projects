package com.github.aqiu202.http.data;

import com.github.aqiu202.http.util.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class HttpResponseEntity<T> {

    private final int statusCode;
    private final T body;
    private final HttpHeaders headers;

    public HttpResponseEntity(int statusCode, T body, HttpHeaders headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String readString(JsonSerializer bodyReader) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("StatusCode：%d", this.statusCode));
        joiner.add(String.format("Headers：%s", this.headers));
        T body = this.body;
        if (body != null) {
            String bodyString;
            if (body instanceof byte[]) {
                bodyString = new String((byte[]) body, StandardCharsets.UTF_8);
            } else if (body instanceof String) {
                bodyString = (String) body;
            } else {
                bodyString = bodyReader.serialize(body);
            }
            joiner.add(String.format("Body：%s", bodyString));
        }
        return joiner.toString();
    }
}
