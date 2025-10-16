package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.Type;

public class WebClientExchanger extends AbstractSpringHttpExchanger<WebClient> {

    public static final WebClientExchanger INSTANCE = new WebClientExchanger();

    @Override
    protected WebClient createRequestInstance() {
        return WebClient.builder()
                .build();
    }

    @Override
    public <T> HttpResponseEntity<T> request(WebClient client, HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        WebClient.RequestBodySpec requestBodySpec = this.beforeRequest(client, request);
        return this.forResponse(request, requestBodySpec, responseType);
    }

    @Override
    public <T> HttpResponseEntity<T> requestWithBody(WebClient client, HttpBodyRequest request, TypeSpec<T> responseType) throws IOException {
        WebClient.RequestBodySpec requestBodySpec = this.beforeRequest(client, request);
        this.resolveRequestBody(requestBodySpec, request);
        return this.forResponse(request, requestBodySpec, responseType);
    }

    public WebClient.RequestBodySpec beforeRequest(WebClient client, HttpRequest<?> request) {
        HttpService.HttpMethod method = request.getMethod();
        return client.method(HttpMethod.valueOf(method.getName()))
                .uri(request.getUri())
                .headers(headers -> {
                    HttpHeaders requestHeaders = request.getHeaders();
                    if (!requestHeaders.isEmpty()) {
                        headers.addAll(new LinkedMultiValueMap<>(requestHeaders.getValues()));
                    }
                });
    }

    public void resolveRequestBody(WebClient.RequestBodySpec requestBodySpec, HttpBodyRequest request) throws IOException {
        Object processedBody = this.processBody(request.getBody());
        if (processedBody != null) {
            requestBodySpec.bodyValue(processedBody);
        }
    }

    public <T> HttpResponseEntity<T> forResponse(HttpRequest<?> request, WebClient.RequestBodySpec requestBodySpec, TypeSpec<T> responseType) {
        ResponseEntity<T> responseEntity = requestBodySpec
                .exchangeToMono(response -> {
                    Type type = responseType.getType();
                    if (type instanceof Class<?>) {
                        return response.toEntity((Class<T>) type);
                    } else {
                        ParameterizedTypeReference<T> typeRef = ParameterizedTypeReference.forType(type);
                        return response.toEntity(typeRef);
                    }
                }).block();
        return this.convertResponse(responseEntity);
    }

}
