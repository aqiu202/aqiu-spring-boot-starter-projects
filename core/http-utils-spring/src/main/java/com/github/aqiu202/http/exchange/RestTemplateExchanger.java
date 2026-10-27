package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

public class RestTemplateExchanger extends AbstractSpringHttpExchanger<RestTemplate> {

    public static final RestTemplateExchanger INSTANCE = new RestTemplateExchanger();

    public static RestTemplateExchanger stateful() {
        return new RestTemplateExchanger(new StatefulRestTemplate());
    }

    public static RestTemplateExchanger stateful(Duration timeout) {
        return new RestTemplateExchanger(new StatefulRestTemplate(timeout));
    }

    private final RestTemplate restTemplate;

    public static RestTemplateExchanger create() {
        return new RestTemplateExchanger();
    }

    public static RestTemplateExchanger create(ClientHttpRequestFactory requestFactory) {
        return new RestTemplateExchanger(requestFactory);
    }

    public static RestTemplateExchanger create(RestTemplate restTemplate) {
        return new RestTemplateExchanger(restTemplate);
    }

    public static RestTemplateExchanger create(Duration connectTimeout, Duration readTimeout) {
        return new RestTemplateExchanger(defaultRequestFactory(connectTimeout, readTimeout));
    }

    /**
     * 默认连接超时 {@link #DEFAULT_CONNECT_TIMEOUT}、读取超时 {@link #DEFAULT_READ_TIMEOUT}，并自动跟随重定向。
     */
    public RestTemplateExchanger() {
        this(defaultRequestFactory(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT));
    }

    public RestTemplateExchanger(RestTemplate restTemplate) {
        Assert.notNull(restTemplate, "restTemplate must not be null");
        this.restTemplate = restTemplate;
    }

    public RestTemplateExchanger(ClientHttpRequestFactory requestFactory) {
        this(new RestTemplate(requestFactory));
    }

    @Override
    protected RestTemplate createRequestInstance() {
        return this.restTemplate;
    }

    @Override
    public <T> HttpResponseEntity<T> request(RestTemplate client, HttpRequest<?> request, TypeSpec<T> responseType) throws RuntimeException {
        HttpEntity<?> httpEntity = this.buildHttpEntity(request);
        return this.doRequest(client, request, httpEntity, responseType);
    }

    @Override
    public <T> HttpResponseEntity<T> requestWithBody(RestTemplate client, HttpBodyRequest request, TypeSpec<T> responseType) throws IOException {
        HttpEntity<?> httpEntity = this.buildHttpEntityWithBody(request);
        return this.doRequest(client, request, httpEntity, responseType);
    }

    public HttpEntity<?> buildHttpEntityWithBody(HttpBodyRequest request) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        Object body = this.processBody(request);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.putAll(httpHeaders.getValues());
        return new HttpEntity<>(body, headers);
    }

    public HttpEntity<?> buildHttpEntity(HttpRequest<?> request) {
        HttpHeaders httpHeaders = request.getHeaders();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.putAll(httpHeaders.getValues());
        return new HttpEntity<>(null, headers);
    }

    protected <T> HttpResponseEntity<T> doRequest(RestTemplate client, HttpRequest<?> request, HttpEntity<?> entity, TypeSpec<T> responseType) {
        HttpService.HttpMethod method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.valueOf(method.getName());
        ResponseEntity<byte[]> responseEntity;
        try {
            responseEntity = client.execute(request.getUri(), httpMethod,
                    this.requestCallback(client, entity),
                    response -> new ResponseEntity<>(response.getBody().readAllBytes(),
                            response.getHeaders(), response.getStatusCode()));
        } catch (RestClientResponseException e) {
            // 4xx/5xx 不抛出异常，与其他实现保持一致，交给上层的重试策略与 responseConsumer 统一处理
            org.springframework.http.HttpHeaders headers = e.getResponseHeaders();
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsByteArray(),
                    headers == null ? new org.springframework.http.HttpHeaders() : headers, e.getStatusCode());
        }
        return this.convertResponse(request, responseEntity, responseType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private RequestCallback requestCallback(RestTemplate client, HttpEntity<?> entity) {
        return request -> {
            request.getHeaders().putAll(entity.getHeaders());
            Object body = entity.getBody();
            if (body == null) {
                return;
            }
            MediaType contentType = entity.getHeaders().getContentType();
            for (HttpMessageConverter<?> converter : client.getMessageConverters()) {
                if (converter.canWrite(body.getClass(), contentType)) {
                    ((HttpMessageConverter) converter).write(body, contentType, request);
                    return;
                }
            }
            throw new RestClientException("无法写入请求体类型：" + body.getClass().getName());
        };
    }

}
