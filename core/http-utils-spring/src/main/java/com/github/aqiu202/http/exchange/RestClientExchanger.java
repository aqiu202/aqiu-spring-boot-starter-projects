package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.time.Duration;

/**
 * 基于 {@link RestClient} 的 {@link HttpExchanger} 实现，用于平替 {@link RestTemplateExchanger}。
 * <p>
 * {@link RestClient} 自 Spring Framework 6.1 引入，是官方推荐的同步 HTTP 客户端，
 * 在保持与 {@link org.springframework.web.client.RestTemplate} 相同同步语义的同时，
 * 提供了流式的 API 风格，且错误处理行为与 {@link WebClientExchanger} 对齐：
 * 非 2xx 响应不会直接抛出异常，而是交给 {@link com.github.aqiu202.http.HttpService}
 * 的 responseConsumer 统一处理。
 */
public class RestClientExchanger extends AbstractSpringHttpExchanger<RestClient> {

    public static final RestClientExchanger INSTANCE = new RestClientExchanger();

    public static RestClientExchanger stateful() {
        return new StatefulRestClientExchanger();
    }

    public static RestClientExchanger stateful(Duration timeout) {
        return new StatefulRestClientExchanger(timeout);
    }

    private final RestClient restClient;

    public static RestClientExchanger create() {
        return new RestClientExchanger();
    }

    public static RestClientExchanger create(ClientHttpRequestFactory requestFactory) {
        return new RestClientExchanger(requestFactory);
    }

    public static RestClientExchanger create(RestClient restClient) {
        return new RestClientExchanger(restClient);
    }

    public static RestClientExchanger create(Duration connectTimeout, Duration readTimeout) {
        return new RestClientExchanger(defaultRequestFactory(connectTimeout, readTimeout));
    }

    /**
     * 默认连接超时 {@link #DEFAULT_CONNECT_TIMEOUT}、读取超时 {@link #DEFAULT_READ_TIMEOUT}，并自动跟随重定向。
     */
    public RestClientExchanger() {
        this(defaultRequestFactory(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT));
    }

    public RestClientExchanger(RestClient restClient) {
        Assert.notNull(restClient, "restClient must not be null");
        this.restClient = restClient;
    }

    public RestClientExchanger(ClientHttpRequestFactory requestFactory) {
        this(RestClient.builder().requestFactory(requestFactory).build());
    }

    @Override
    protected RestClient createRequestInstance() {
        return this.restClient;
    }

    @Override
    public <T> HttpResponseEntity<T> request(RestClient client, HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        RestClient.RequestBodySpec requestSpec = this.beforeRequest(client, request);
        return this.forResponse(request, requestSpec, responseType);
    }

    @Override
    public <T> HttpResponseEntity<T> requestWithBody(RestClient client, HttpBodyRequest request, TypeSpec<T> responseType) throws IOException {
        RestClient.RequestBodySpec requestSpec = this.beforeRequest(client, request);
        this.resolveRequestBody(requestSpec, request);
        return this.forResponse(request, requestSpec, responseType);
    }

    public RestClient.RequestBodySpec beforeRequest(RestClient client, HttpRequest<?> request) {
        HttpService.HttpMethod method = request.getMethod();
        return client.method(HttpMethod.valueOf(method.getName()))
                .uri(request.getUri())
                .headers(headers -> {
                    HttpHeaders requestHeaders = request.getHeaders();
                    if (!requestHeaders.isEmpty()) {
                        requestHeaders.getValues().forEach(headers::addAll);
                    }
                });
    }

    public void resolveRequestBody(RestClient.RequestBodySpec requestSpec, HttpBodyRequest request) throws IOException {
        Object processedBody = this.processBody(request);
        if (processedBody != null) {
            requestSpec.body(processedBody);
        }
    }

    public <T> HttpResponseEntity<T> forResponse(HttpRequest<?> request, RestClient.RequestBodySpec requestSpec,
                                                 TypeSpec<T> responseType) {
        // 直接读取原始字节，避免 HTTP 状态或非法响应 charset 在核心转换前被处理。
        ResponseEntity<byte[]> responseEntity = requestSpec.exchange((req, res) ->
                new ResponseEntity<>(res.getBody().readAllBytes(), res.getHeaders(), res.getStatusCode()), false);
        return this.convertResponse(request, responseEntity, responseType);
    }

}
