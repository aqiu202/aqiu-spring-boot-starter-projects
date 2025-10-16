package com.github.aqiu202.http;

import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.data.HttpQueries;
import com.github.aqiu202.http.intercept.HttpInterceptor;
import com.github.aqiu202.http.util.JsonSerializer;
import com.github.aqiu202.http.util.RequestDescriptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface HttpService {

    default HttpBodyRequest method(HttpMethod method) {
        return new HttpBodyRequest(this.buildRequestDescriptor(method));
    }

    JsonSerializer getJsonSerializer();

    String getBaseUrl();

    HttpHeaders getDefaultHeaders();

    HttpQueries getDefaultQueryParams();

    HttpExchanger getExchanger();

    List<HttpInterceptor> getInterceptors();

    BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> getResponseConsumer();

    boolean isEnableDebug();

    default RequestDescriptor buildRequestDescriptor(HttpMethod method) {
        return new RequestDescriptor(
                this.isEnableDebug(),
                this.getBaseUrl(),
                method,
                this.getDefaultHeaders(),
                this.getDefaultQueryParams(),
                this.getExchanger(),
                this.getInterceptors(),
                this.getJsonSerializer(),
                this.getResponseConsumer());
    }

    default HttpRequest<?> get() {
        return this.method(HttpMethod.GET);
    }

    default HttpBodyRequest post() {
        return this.method(HttpMethod.POST);
    }

    default HttpBodyRequest put() {
        return this.method(HttpMethod.PUT);
    }

    default HttpBodyRequest delete() {
        return this.method(HttpMethod.DELETE);
    }

    default HttpBodyRequest patch() {
        return this.method(HttpMethod.PATCH);
    }

    static Builder builder() {
        return new DefaultHttpServiceBuilder();
    }

    interface Builder {

        Builder debug(boolean enableDebug);

        default Builder enableDebug() {
            return this.debug(true);
        }

        default Builder disableDebug() {
            return this.debug(false);
        }

        Builder jsonSerializer(JsonSerializer jsonSerializer);

        Builder baseUrl(String baseUrl);

        Builder defaultHeader(String name, Object... values);

        Builder defaultHeaders(HttpHeaders headers);

        Builder defaultHeaders(Consumer<HttpHeaders> headersConsumer);

        Builder defaultQueryParam(String key, Object... value);

        Builder defaultQueryParams(HttpQueries params);

        Builder defaultQueryParams(Consumer<HttpQueries> paramsConsumer);

        default Builder addInterceptors(HttpInterceptor... interceptors) {
            return this.addInterceptors(Arrays.asList(interceptors));
        }

        Builder addInterceptors(Collection<HttpInterceptor> interceptors);

        Builder exchanger(HttpExchanger agent);

        Builder onResponse(BiConsumer<HttpRequest<?>, HttpResponseEntity<?>> responseConsumer);

        HttpService build();
    }

    enum HttpMethod {

        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        PATCH("PATCH"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        TRACE("TRACE");

        private final String name;

        HttpMethod(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static HttpMethod fromString(String name) {
            for (HttpMethod method : HttpMethod.values()) {
                if (method.getName().equalsIgnoreCase(name)) {
                    return method;
                }
            }
            throw new IllegalArgumentException("Invalid HTTP method: " + name);
        }
    }
}
