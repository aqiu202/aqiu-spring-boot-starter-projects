package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;

public class RestTemplateExchanger extends AbstractSpringHttpExchanger<RestTemplate> {

    public static final RestTemplateExchanger INSTANCE = new RestTemplateExchanger();

    private final ClientHttpRequestFactory requestFactory;

    public RestTemplateExchanger() {
        this(new JdkClientHttpRequestFactory());
    }

    public RestTemplateExchanger(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "requestFactory must not be null");
        this.requestFactory = requestFactory;
    }

    @Override
    protected RestTemplate createRequestInstance() {
        return new RestTemplate(this.requestFactory);
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
        Object body = this.processBody(request.getBody());
        return new HttpEntity<>(body, new LinkedMultiValueMap<>(httpHeaders.getValues()));
    }

    public HttpEntity<?> buildHttpEntity(HttpRequest<?> request) {
        HttpHeaders httpHeaders = request.getHeaders();
        return new HttpEntity<>(null, new LinkedMultiValueMap<>(httpHeaders.getValues()));
    }

    protected <T> HttpResponseEntity<T> doRequest(RestTemplate client, HttpRequest<?> request, HttpEntity<?> entity, TypeSpec<T> responseType) {
        HttpService.HttpMethod method = request.getMethod();
        ResponseEntity<T> responseEntity;
        HttpMethod httpMethod = HttpMethod.valueOf(method.getName());
        Type type = responseType.getType();
        if (type instanceof Class<?>) {
            responseEntity = client.exchange(request.getUri(), httpMethod, entity, (Class<T>) type);
        } else {
            ParameterizedTypeReference<T> typeRef = ParameterizedTypeReference.forType(type);
            responseEntity = client.exchange(request.getUri(), httpMethod, entity, typeRef);
        }
        return this.convertResponse(responseEntity);
    }

}
