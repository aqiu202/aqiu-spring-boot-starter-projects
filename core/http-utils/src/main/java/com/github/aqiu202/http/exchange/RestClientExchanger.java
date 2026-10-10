package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
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
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        return new RestClientExchanger(requestFactory);
    }

    public RestClientExchanger() {
        this(new JdkClientHttpRequestFactory());
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
        return this.forResponse(requestSpec, responseType);
    }

    @Override
    public <T> HttpResponseEntity<T> requestWithBody(RestClient client, HttpBodyRequest request, TypeSpec<T> responseType) throws IOException {
        RestClient.RequestBodySpec requestSpec = this.beforeRequest(client, request);
        this.resolveRequestBody(requestSpec, request);
        return this.forResponse(requestSpec, responseType);
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
        Object processedBody = this.processBody(request.getBody());
        if (processedBody != null) {
            requestSpec.body(processedBody);
        }
    }

    public <T> HttpResponseEntity<T> forResponse(RestClient.RequestBodySpec requestSpec, TypeSpec<T> responseType) {
        Type type = responseType.getType();
        // 覆盖默认的 4xx/5xx 异常处理，交给上层的 responseConsumer 统一处理
        RestClient.ResponseSpec responseSpec = requestSpec
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                });
        ResponseEntity<T> responseEntity;
        if (type instanceof Class<?>) {
            responseEntity = responseSpec.toEntity((Class<T>) type);
        } else {
            responseEntity = responseSpec.toEntity(ParameterizedTypeReference.<T>forType(type));
        }
        return this.convertResponse(responseEntity);
    }

}
