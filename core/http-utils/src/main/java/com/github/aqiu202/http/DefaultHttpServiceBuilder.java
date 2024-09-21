package com.github.aqiu202.http;

import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.exchange.WebClientExchanger;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.util.JacksonSerializer;
import com.github.aqiu202.http.util.JsonSerializer;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DefaultHttpServiceBuilder implements HttpService.Builder {

    private boolean enableDebug;
    private String baseUrl;
    private final HttpHeaders defaultHeaders = new HttpHeaders();
    private final HttpQueries defaultQueryParams = new HttpQueries();
    private final List<HttpInterceptor> interceptors = new ArrayList<>();
    private JsonSerializer jsonSerializer = JacksonSerializer.INSTANCE;
    private HttpExchanger exchanger = WebClientExchanger.INSTANCE;
    private BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer = (request, responseEntity) -> {
        int statusCode = responseEntity.getStatusCode();
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        if (!httpStatus.is2xxSuccessful()) {
            throw new RuntimeException("HTTP请求失败：" + httpStatus.getReasonPhrase());
        }
    };

    @Override
    public HttpService.Builder debug(boolean enableDebug) {
        this.enableDebug = enableDebug;
        return this;
    }

    @Override
    public HttpService.Builder jsonSerializer(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
        return this;
    }

    @Override
    public HttpService.Builder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public HttpService.Builder defaultHeader(String name, Object... values) {
        this.defaultHeaders.set(name, values);
        return this;
    }

    @Override
    public HttpService.Builder defaultHeaders(HttpHeaders headers) {
        this.defaultHeaders.addAll(headers);
        return this;
    }

    @Override
    public HttpService.Builder defaultHeaders(Consumer<HttpHeaders> headersConsumer) {
        headersConsumer.accept(this.defaultHeaders);
        return this;
    }

    @Override
    public HttpService.Builder defaultQueryParam(String key, Object... value) {
        this.defaultQueryParams.set(key, value);
        return this;
    }

    @Override
    public HttpService.Builder defaultQueryParams(HttpQueries params) {
        this.defaultQueryParams.addAll(params);
        return this;
    }

    @Override
    public HttpService.Builder defaultQueryParams(Consumer<HttpQueries> paramsConsumer) {
        paramsConsumer.accept(this.defaultQueryParams);
        return this;
    }

    @Override
    public HttpService.Builder addInterceptors(Collection<HttpInterceptor> interceptors) {
        if (interceptors != null) {
            this.interceptors.addAll(interceptors);
        }
        return this;
    }

    @Override
    public HttpService.Builder exchanger(HttpExchanger exchanger) {
        this.exchanger = exchanger;
        return this;
    }

    @Override
    public HttpService.Builder onResponse(BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer) {
        this.responseConsumer = responseConsumer;
        return this;
    }

    @Override
    public HttpService build() {
        return new DefaultHttpService(this.enableDebug, this.baseUrl, this.jsonSerializer,
                this.defaultHeaders, this.defaultQueryParams, this.exchanger,
                this.interceptors, this.responseConsumer);
    }
}
