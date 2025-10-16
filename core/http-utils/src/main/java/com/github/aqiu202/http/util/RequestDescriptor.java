package com.github.aqiu202.http.util;

import com.github.aqiu202.http.*;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class RequestDescriptor {
    private boolean enableDebug;
    private String baseUrl;
    private HttpService.HttpMethod method;
    private HttpHeaders defaultHeaders;
    private HttpQueries defaultQueryParams;
    private HttpExchanger exchanger;
    private List<HttpInterceptor> interceptors;
    private Predicate<HttpStatus> statusPredicate;
    private JsonSerializer jsonSerializer;
    private BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer;

    public RequestDescriptor() {
    }

    public RequestDescriptor(boolean enableDebug, String baseUrl,
                             HttpService.HttpMethod method, HttpHeaders defaultHeaders,
                             HttpQueries defaultQueryParams, HttpExchanger exchanger,
                             List<HttpInterceptor> interceptors, JsonSerializer jsonSerializer,
                             BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer) {
        this.enableDebug = enableDebug;
        this.baseUrl = baseUrl;
        this.method = method;
        this.defaultHeaders = defaultHeaders;
        this.defaultQueryParams = defaultQueryParams;
        this.exchanger = exchanger;
        this.interceptors = interceptors;
        this.responseConsumer = responseConsumer;
        this.jsonSerializer = jsonSerializer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HttpService.HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpService.HttpMethod method) {
        this.method = method;
    }

    public JsonSerializer getJsonSerializer() {
        return jsonSerializer;
    }

    public void setJsonSerializer(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setDefaultHeaders(HttpHeaders defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public HttpQueries getDefaultQueryParams() {
        return defaultQueryParams;
    }

    public void setDefaultQueryParams(HttpQueries defaultQueryParams) {
        this.defaultQueryParams = defaultQueryParams;
    }

    public HttpExchanger getExchanger() {
        return exchanger;
    }

    public void setExchanger(HttpExchanger exchanger) {
        this.exchanger = exchanger;
    }

    public List<HttpInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<HttpInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public Predicate<HttpStatus> getStatusPredicate() {
        return statusPredicate;
    }

    public void setStatusPredicate(Predicate<HttpStatus> statusPredicate) {
        this.statusPredicate = statusPredicate;
    }

    public BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> getResponseConsumer() {
        return responseConsumer;
    }

    public void setResponseConsumer(BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer) {
        this.responseConsumer = responseConsumer;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }
}
