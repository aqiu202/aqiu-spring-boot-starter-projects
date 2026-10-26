package com.github.aqiu202.http;

import com.github.aqiu202.http.util.*;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.intercept.InterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private RetryPolicy retryPolicy;
    private final RequestDescriptor descriptor;
    private String traceId;

    private static final String PREFIX_HTTP = "http://";
    private static final String PREFIX_HTTPS = "https://";

    private static boolean isAbsoluteUrl(String url) {
        return url.startsWith(PREFIX_HTTP) || url.startsWith(PREFIX_HTTPS);
    }

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
        this.retryPolicy = descriptor.getRetryPolicy();
        this.enableDebug = descriptor.isEnableDebug();
    }

    public HttpService.HttpMethod getMethod() {
        return method;
    }

    public JsonSerializer getJsonSerializer() {
        return this.descriptor.getJsonSerializer();
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

    /**
     * 追加多值查询参数：集合中的每个元素都会生成一个同名参数。
     */
    public T addQueryParam(String name, Collection<?> values) {
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
        if (isAbsoluteUrl(path)) {
            return this.uri(path, uriVariables);
        }
        this.uriBuilder.path(path);
        return this.uriVariables(uriVariables);
    }

    /**
     * 按分段设置路径：每个分段独立编码后再以 {@code /} 连接，适合携带动态 id 等未编码值。
     */
    public T pathSegments(String... segments) {
        this.uriBuilder.pathSegments(segments);
        return (T) this;
    }

    public T pathSegments(Collection<String> segments) {
        this.uriBuilder.pathSegments(segments);
        return (T) this;
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

    /**
     * 设置条件重试策略；在响应消费者之前判定，命中且未超过次数时重发当前请求。
     * <p>
     * 优先级：单个请求设置的重试策略覆盖 {@link HttpService.Builder#retry(RetryPolicy)}
     * 配置的服务级策略；未设置时沿用服务级策略。
     */
    public T retry(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return (T) this;
    }

    public RetryPolicy getRetryPolicy() {
        return this.retryPolicy;
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

    public <S> Optional<S> tryExchange(Class<S> responseType) {
        return Optional.ofNullable(this.exchange(responseType));
    }

    public <S> Optional<S> tryExchange(ParameterizedTypeRef<S> responseType) {
        return Optional.ofNullable(this.exchange(responseType));
    }

    /**
     * 自定义反序列化
     *
     * @param customizer 自定义反序列化逻辑
     * @return 返回值
     */
    public <S> S exchangeForType(Function<byte[], S> customizer) {
        byte[] original = this.exchange(byte[].class);
        return customizer.apply(original);
    }

    /**
     * 强制反序列化（从原始响应文本二次反序列化）
     *
     * @param responseType 目标类型
     * @return 返回值
     */
    public <S> S exchangeForType(Class<S> responseType) {
        return this.descriptor.getJsonSerializer().deserialize(this.exchange(String.class), responseType);
    }

    /**
     * 强制反序列化（从原始响应文本二次反序列化）
     *
     * @param responseType 目标类型
     * @return 返回值
     */
    public <S> S exchangeForType(ParameterizedTypeRef<S> responseType) {
        return this.descriptor.getJsonSerializer()
                .deserialize(this.exchange(String.class), responseType.getType());
    }

    public <S> S exchange(ParameterizedTypeRef<S> responseType) {
        return this.exchange(new TypeSpec<>(responseType));
    }

    public <S> S exchange(TypeSpec<S> responseType) {
        HttpResponseEntity<S> response = this.exchangeForResponse(responseType);
        return response.getBody();
    }

    public HttpResponseEntity<String> exchangeForResponse() {
        return this.exchangeForResponse(String.class);
    }

    public <S> HttpResponseEntity<S> exchangeForResponse(ParameterizedTypeRef<S> responseType) {
        return this.exchangeForResponse(new TypeSpec<>(responseType));
    }

    public <S> HttpResponseEntity<S> exchangeForResponse(Class<S> responseType) {
        return this.exchangeForResponse(new TypeSpec<>(responseType));
    }

    public <S> HttpResponseEntity<S> exchangeForResponse(TypeSpec<S> responseType) {
        try {
            List<HttpInterceptor> resolvedInterceptors = this.resolveInterceptors();
            this.processInterceptors(resolvedInterceptors);
            if (this.enableDebug || log.isDebugEnabled()) {
                this.printRequestDebugInfo();
            }
            int attempt = 0;
            while (true) {
                HttpResponseEntity<S> response = this.exchanger.exchange(this, responseType);
                if (this.enableDebug || log.isDebugEnabled()) {
                    this.printResponseDebugInfo(response);
                }
                if (this.retryPolicy == null || !this.retryPolicy.matches(response)
                        || attempt >= this.retryPolicy.getMaxRetries()) {
                    this.invokeResponseConsumer(this, response);
                    return response;
                }
                attempt++;
                this.retryPolicy.beforeRetry(this);
            }
        } catch (IOException e) {
            throw new RuntimeException("Http请求异常", e);
        }
    }

    protected void processInterceptors(List<HttpInterceptor> interceptors) {
        if (interceptors != null && !interceptors.isEmpty()) {
            InterceptorContext interceptorContext = this.buildInterceptorContext();
            for (HttpInterceptor interceptor : interceptors) {
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

    protected List<HttpInterceptor> resolveInterceptors() {
        // 始终基于副本排序，避免并发请求同时修改服务级共享列表
        List<HttpInterceptor> resolvedInterceptors = this.interceptors == null
                ? new ArrayList<>() : new ArrayList<>(this.interceptors);
        if (this.optionsConsumer != null || this.headersConsumer != null) {
            resolvedInterceptors.add(new ConfigureInterceptor((Consumer<HttpRequest<?>>) this.optionsConsumer, this.headersConsumer));
        }
        if (!resolvedInterceptors.isEmpty()) {
            resolvedInterceptors.sort(Comparator.comparingInt(HttpInterceptor::getOrder));
        }
        return resolvedInterceptors;
    }

    private String getTraceId() {
        if (this.traceId == null) {
            this.traceId = this.generateTraceId();
        }
        return this.traceId;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void printRequestDebugInfo() {
        this.log.debug("\n**************************************************************************\n" +
                        "**********     HttpService ---- HTTP Request Info      *************\n" +
                        "**********     TraceID ---- {}      *************\n" +
                        "{}\n" +
                        "**************************************************************************",
                this.getTraceId(), this);
    }

    private void printResponseDebugInfo(HttpResponseEntity<?> response) {
        this.log.debug("\n**************************************************************************\n" +
                        "**********     HttpService ---- HTTP Response Info      *************\n" +
                        "**********     TraceID ---- {}      *************\n" +
                        "{}\n" +
                        "**************************************************************************",
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

    static class ConfigureInterceptor implements HttpInterceptor {
        private final Consumer<HttpRequest<?>> optionsConsumer;
        private final Consumer<HttpHeaders> headersConsumer;

        public ConfigureInterceptor(Consumer<HttpRequest<?>> optionsConsumer, Consumer<HttpHeaders> headersConsumer) {
            this.optionsConsumer = optionsConsumer;
            this.headersConsumer = headersConsumer;
        }

        @Override
        public void intercept(InterceptorContext context, HttpRequest<?> request) {
            if (this.headersConsumer != null)
                this.headersConsumer.accept(request.getHeaders()
                );
            if (this.optionsConsumer != null) {
                this.optionsConsumer.accept(request);
            }
        }

        @Override
        public int getOrder() {
            return DEFAULT_CONFIGURE_ORDER;
        }
    }

}
