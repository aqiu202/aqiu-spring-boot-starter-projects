package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.*;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;

import java.io.IOException;

public abstract class AbstractHttpExchanger<D> implements HttpExchanger {

    private volatile D requestInstance;

    public D getRequestInstance() {
        if (this.requestInstance == null) {
            synchronized (this) {
                if (this.requestInstance == null) {
                    this.requestInstance = this.createRequestInstance();
                }
            }
        }
        return this.requestInstance;
    }

    protected abstract D createRequestInstance();

    @Override
    public <T> HttpResponseEntity<T> exchange(HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        return this.doExchange(this.getRequestInstance(), request, responseType);
    }

    public <T> HttpResponseEntity<T> doExchange(D instance, HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        HttpService.HttpMethod method = request.getMethod();
        switch (method) {
            case GET:
            case OPTIONS:
            case TRACE:
                return this.request(instance, request, responseType);
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                return this.requestWithBody(instance, ((HttpBodyRequest) request), responseType);
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    public abstract <T> HttpResponseEntity<T> request(D client, HttpRequest<?> request, TypeSpec<T> responseType) throws IOException;

    public abstract <T> HttpResponseEntity<T> requestWithBody(D client, HttpBodyRequest request, TypeSpec<T> responseType) throws IOException;

}
