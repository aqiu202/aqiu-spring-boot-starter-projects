package com.github.aqiu202.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.intercept.InterceptorContext;
import com.github.aqiu202.http.util.ParameterizedTypeRef;
import com.github.aqiu202.http.util.RequestDescriptor;
import com.github.aqiu202.http.util.TypeSpec;
import com.github.aqiu202.http.util.URIBuilder;
import com.github.aqiu202.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class HttpRequest<T extends HttpRequest<?>> {

    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpService.HttpMethod method;
    private final URIBuilder uriBuilder = new URIBuilder();
    private final HttpHeaders headers;
    private final HttpExchanger exchanger;
    private final List<HttpInterceptor> interceptors;
    private Consumer<T> optionsConsumer;
    private Consumer<HttpHeaders> headersConsumer;
    private boolean enableDebug;
    private BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer;
    private final RequestDescriptor descriptor;
    private String traceId;

    public HttpRequest(RequestDescriptor descriptor) {
        this.descriptor = descriptor;
        this.uriBuilder
                .baseUrl(descriptor.getBaseUrl())
                .addQueryParams(descriptor.getDefaultQueryParams());
        this.method = descriptor.getMethod();
        this.headers = descriptor.getDefaultHeaders().deepClone();
        this.exchanger = descriptor.getExchanger();
        this.interceptors = descriptor.getInterceptors();
        this.responseConsumer = descriptor.getResponseConsumer();
        this.enableDebug = descriptor.isEnableDebug();
    }

    public HttpService.HttpMethod getMethod() {
        return method;
    }

    public T addHeader(String name, Object... values) {
        this.headers.add(name, values);
        return (T) this;
    }

    public T addHeaders(HttpHeaders headers) {
        this.headers.addAll(headers);
        return (T) this;
    }

    public T setHeader(String name, Object... values) {
        this.headers.set(name, values);
        return (T) this;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public T setRawQuery(String rawQuery) {
        this.uriBuilder.query(rawQuery);
        return (T) this;
    }

    public T addQueryBean(Object bean) {
        this.uriBuilder.addQueryBean(bean);
        return (T) this;
    }

    public T addQueryParam(String name, Object... values) {
        this.uriBuilder.addQueryParam(name, values);
        return (T) this;
    }

    public T replaceQueryParam(String name, Object... values) {
        this.uriBuilder.setQueryParam(name, values);
        return (T) this;
    }

    public T replaceQueryParams(HttpQueries queryParams) {
        this.uriBuilder.setQueryParams(queryParams);
        return (T) this;
    }

    public T addQueryParams(HttpQueries queryParams) {
        this.uriBuilder.addQueryParams(queryParams);
        return (T) this;
    }

    public T uri(String uri, Object... uriVariables) {
        this.uriBuilder.uri(uri);
        return this.uriVariables(uriVariables);
    }

    public T uri(URI uri) {
        this.uriBuilder.uri(uri);
        return (T) this;
    }

    public T path(String path, Object... uriVariables) {
        this.uriBuilder.path(path);
        return this.uriVariables(uriVariables);
    }

    public T uriVariable(String name, Object value) {
        this.uriBuilder.uriVariable(name, value);
        return (T) this;
    }

    public T uriVariables(Map<String, ?> uriVariables) {
        this.uriBuilder.uriVariables(uriVariables);
        return (T) this;
    }

    public T uriVariables(Object... uriVariables) {
        this.uriBuilder.uriVariables(uriVariables);
        return (T) this;
    }

    public T contentType(String contentType) {
        this.getHeaders().setContentType(contentType);
        return (T) this;
    }

    public T contentLength(long contentLength) {
        this.getHeaders().setContentLength(contentLength);
        return (T) this;
    }

    public T headers(Consumer<HttpHeaders> headersConsumer) {
        this.headersConsumer = headersConsumer;
        return (T) this;
    }

    public T configure(Consumer<T> optionsConsumer) {
        this.optionsConsumer = optionsConsumer;
        return (T) this;
    }

    public T onResponse(BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer) {
        this.responseConsumer = responseConsumer;
        return (T) this;
    }

    public T debug(boolean enableDebug) {
        this.enableDebug = enableDebug;
        return (T) this;
    }

    public BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> getResponseConsumer() {
        return this.responseConsumer;
    }

    public URI getUri() {
        return this.uriBuilder.build();
    }

    public <S> S exchange(Class<S> responseType) {
        return this.exchange(new TypeSpec<>(responseType));
    }

    public T enableDebug() {
        return this.debug(true);
    }

    public T disableDebug() {
        return this.debug(false);
    }

    public String getUriString() {
        return this.getUri().toString();
    }

    public String exchange() {
        return this.exchange(String.class);
    }

    public Optional<String> tryExchange() {
        return Optional.ofNullable(this.exchange());
    }

    public<S> Optional<S> tryExchange(Class<S> responseType) {
        return Optional.ofNullable(this.exchange(responseType));
    }

    public<S> Optional<S> tryExchange(ParameterizedTypeRef<S> responseType) {
        return Optional.ofNullable(this.exchange(responseType));
    }

    /**
     * 自定义反序列化
     * @param customizer 自定义反序列化逻辑
     * @return 返回值
     */
    public<S> S exchangeForType(Function<byte[], S> customizer) {
        byte[] original = this.exchange(byte[].class);
        return customizer.apply(original);
    }

    /**
     * 强制反序列化（从字节数组二次反序列化）
     * @param responseType 目标类型
     * @return 返回值
     */
    public<S> S exchangeForType(Class<S> responseType) {
        byte[] original = this.exchange(byte[].class);
        return JacksonUtils.toObject(original, responseType);
    }

    /**
     * 强制反序列化（从字节数组二次反序列化）
     * @param responseType 目标类型
     * @return 返回值
     */
    public<S> S exchangeForType(ParameterizedTypeRef<S> responseType) {
        byte[] original = this.exchange(byte[].class);
        return JacksonUtils.toObject(original, new TypeReference<S>() {
            @Override
            public Type getType() {
                return responseType.getType();
            }
        });
    }
    public <S> S exchange(ParameterizedTypeRef<S> responseType) {
        return this.exchange(new TypeSpec<>(responseType));
    }

    public <S> S exchange(TypeSpec<S> responseType) {
        try {
            this.processInterceptors();
            this.invokeConsumers();
            if (this.enableDebug) {
                this.printRequestDebugInfo();
            }
            HttpResponseEntity<S> response = this.exchanger.exchange(this, responseType);
            if (this.enableDebug) {
                this.printResponseDebugInfo(response);
            }
            this.invokeResponseConsumer(this, response);
            return response.getBody();
        } catch (IOException e) {
            throw new RuntimeException("Http请求异常", e);
        }
    }

    protected void processInterceptors() {
        if (this.interceptors != null && !this.interceptors.isEmpty()) {
            InterceptorContext interceptorContext = this.buildInterceptorContext();
            for (HttpInterceptor interceptor : this.interceptors) {
                interceptor.intercept(interceptorContext, this);
            }
        }
    }

    protected void invokeResponseConsumer(HttpRequest<?> request, HttpResponseEntity<?> responseEntity) {
        BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer = request.getResponseConsumer();
        if (responseConsumer != null) {
            responseConsumer.accept(request, responseEntity);
        }
    }

    protected InterceptorContext buildInterceptorContext() {
        return InterceptorContext.fromThreadContext();
    }

    protected void invokeConsumers() {
        if (this.optionsConsumer != null) {
            this.optionsConsumer.accept((T) this);
        }
        if (this.headersConsumer != null) {
            this.headersConsumer.accept(this.getHeaders());
        }
    }

    private String getTraceId() {
        if (this.traceId == null) {
            this.traceId = this.generateTraceId();
        }
        return this.traceId;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    private void printRequestDebugInfo() {
        this.log.warn("\n**************************************************************************\n" +
                        "                        **********     HttpService ---- HTTP Request Info      *************\n" +
                        "                        **********     Exchanger ---- {}      *************\n" +
                        "                        **********     TraceID ---- {}      *************\n" +
                        "                        {}\n" +
                        "                        **************************************************************************",
                this.getExchangerName(), this.getTraceId(), this);
    }

    private void printResponseDebugInfo(HttpResponseEntity<?> response) {
        this.log.warn("\n**************************************************************************\n" +
                        "                        **********     HttpService ---- HTTP Response Info      *************\n" +
                        "                        **********     TraceID ---- {}      *************\n" +
                        "                        {}\n" +
                        "                        **************************************************************************",
                this.getTraceId(), this.getResponseString(response));
    }

    private String getResponseString(HttpResponseEntity<?> response) {
        return response.readString(this.descriptor.getJsonSerializer());
    }

    private String getExchangerName() {
        return this.exchanger.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Method：%s", this.getMethod()));
        joiner.add(String.format("Url：%s", this.getUriString()));
        joiner.add(String.format("Headers：%s", this.getHeaders()));
        return joiner.toString();
    }

}
