package com.github.aqiu202.http;

import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.util.JsonSerializer;
import com.github.aqiu202.http.util.RetryPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class DefaultHttpService implements HttpService {

    private final boolean enableDebug;
    private final String baseUrl;
    private final JsonSerializer jsonSerializer;
    private final HttpHeaders defaultHeaders;
    private final HttpQueries defaultQueryParams;
    private final HttpExchanger exchanger;
    private final List<HttpInterceptor> interceptors;
    private final BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer;
    private final RetryPolicy retryPolicy;

    public DefaultHttpService(boolean enableDebug,
                              String baseUrl,
                              JsonSerializer jsonSerializer,
                              HttpHeaders defaultHeaders,
                              HttpQueries defaultQueryParams, HttpExchanger exchanger,
                              List<HttpInterceptor> interceptors,
                              BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer,
                              RetryPolicy retryPolicy) {
        this.enableDebug = enableDebug;
        this.baseUrl = baseUrl;
        this.jsonSerializer = jsonSerializer;
        this.defaultHeaders = defaultHeaders;
        this.defaultQueryParams = defaultQueryParams;
        this.exchanger = exchanger;
        if (interceptors == null) {
            this.interceptors = Collections.emptyList();
        } else {
            List<HttpInterceptor> copy = new ArrayList<>(interceptors);
            copy.forEach(interceptor -> Objects.requireNonNull(interceptor, "interceptor must not be null"));
            this.interceptors = Collections.unmodifiableList(copy);
        }
        this.responseConsumer = responseConsumer;
        this.retryPolicy = retryPolicy;
    }

    @Override
    public boolean isEnableDebug() {
        return enableDebug;
    }

    @Override
    public JsonSerializer getJsonSerializer() {
        return this.jsonSerializer;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
    }

    @Override
    public HttpQueries getDefaultQueryParams() {
        return defaultQueryParams;
    }

    public HttpExchanger getExchanger() {
        return exchanger;
    }

    @Override
    public List<HttpInterceptor> getInterceptors() {
        return this.interceptors;
    }

    @Override
    public BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> getResponseConsumer() {
        return responseConsumer;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return this.retryPolicy;
    }
}
