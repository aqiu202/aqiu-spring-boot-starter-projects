package com.github.aqiu202.http.data;

import com.github.aqiu202.http.util.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/**
 * HTTP 响应。
 * <p>
 * 除转换后的 {@link #getBody() body} 外，还保留原始响应文本 {@link #getRawBody() rawBody}：
 * 空 body 或非 JSON 响应不会中断请求，转换失败时 {@code body} 为 {@code null}，
 * 调用方可读取原文并通过 {@link #getConversionError() conversionError} 判断失败原因。
 */
public class HttpResponseEntity<T> {

    private final int statusCode;
    private final T body;
    private final HttpHeaders headers;
    private final String rawBody;
    private final RuntimeException conversionError;

    public HttpResponseEntity(int statusCode, T body, HttpHeaders headers) {
        this(statusCode, body, headers, null, null);
    }

    public HttpResponseEntity(int statusCode, T body, HttpHeaders headers, String rawBody) {
        this(statusCode, body, headers, rawBody, null);
    }

    public HttpResponseEntity(int statusCode, T body, HttpHeaders headers, String rawBody,
                              RuntimeException conversionError) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.rawBody = rawBody;
        this.conversionError = conversionError;
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

    /**
     * 原始响应文本；空 body 时为 {@code null}。
     */
    public String getRawBody() {
        return rawBody;
    }

    public boolean hasRawBody() {
        return this.rawBody != null && !this.rawBody.isEmpty();
    }

    /**
     * 响应体转换失败的原因；转换成功或空 body 时为 {@code null}。
     */
    public RuntimeException getConversionError() {
        return conversionError;
    }

    public String readString(JsonSerializer bodyReader) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("StatusCode：%d", this.statusCode));
        joiner.add(String.format("Headers：%s", this.headers));
        String bodyString = this.rawBody;
        T body = this.body;
        if (bodyString == null && body != null) {
            if (body instanceof byte[]) {
                bodyString = new String((byte[]) body, StandardCharsets.UTF_8);
            } else if (body instanceof String) {
                bodyString = (String) body;
            } else {
                bodyString = bodyReader.serialize(body);
            }
        }
        if (bodyString != null) {
            joiner.add(String.format("Body：%s", bodyString));
        }
        return joiner.toString();
    }
}
