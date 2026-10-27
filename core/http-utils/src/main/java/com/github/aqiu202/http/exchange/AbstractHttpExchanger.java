package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.*;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

public abstract class AbstractHttpExchanger<D> implements HttpExchanger {

    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(60);
    public static final Duration DEFAULT_CONNECTION_REQUEST_TIMEOUT = Duration.ofSeconds(30);

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

    /**
     * 返回已经初始化的客户端，不触发懒加载。
     */
    protected final D getInitializedRequestInstance() {
        return this.requestInstance;
    }

    @Override
    public <T> HttpResponseEntity<T> exchange(HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        return this.doExchange(this.getRequestInstance(), request, responseType);
    }

    public <T> HttpResponseEntity<T> doExchange(D instance, HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        HttpService.HttpMethod method = request.getMethod();
        switch (method) {
            case GET:
            case HEAD:
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

    /**
     * 按原始字节保留响应文本，再转换为目标类型；HTTP 状态不在此处转为异常。
     */
    @SuppressWarnings("unchecked")
    protected <S> HttpResponseEntity<S> convertResponse(HttpRequest<?> request, int statusCode,
                                                        HttpHeaders headers, byte[] bytes,
                                                        TypeSpec<S> responseType) {
        HttpHeaders responseHeaders = headers == null ? new HttpHeaders() : headers;
        if (bytes != null && bytes.length == 0) {
            bytes = null;
        }
        String rawBody = bytes == null ? null : new String(bytes, resolveCharset(responseHeaders));
        Type type = responseType.getType();
        S body = null;
        RuntimeException conversionError = null;
        if (type == byte[].class) {
            body = (S) bytes;
        } else if (type == String.class) {
            body = (S) rawBody;
        } else if (rawBody != null && !rawBody.trim().isEmpty()) {
            try {
                body = request.getJsonSerializer().deserialize(rawBody, type);
            } catch (RuntimeException e) {
                conversionError = e;
            }
        }
        return new HttpResponseEntity<>(statusCode, body, responseHeaders, rawBody, conversionError);
    }

    private static Charset resolveCharset(HttpHeaders headers) {
        String contentType = headers.getFirst("Content-Type");
        if (contentType != null) {
            String[] parameters = contentType.split(";");
            for (int i = 1; i < parameters.length; i++) {
                String parameter = parameters[i].trim();
                int separator = parameter.indexOf('=');
                if (separator > 0 && "charset".equals(parameter.substring(0, separator)
                        .trim().toLowerCase(Locale.ROOT))) {
                    String value = parameter.substring(separator + 1).trim();
                    if (value.length() > 1 && value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    try {
                        return Charset.forName(value);
                    } catch (IllegalArgumentException ignored) {
                        return StandardCharsets.UTF_8;
                    }
                }
            }
        }
        return StandardCharsets.UTF_8;
    }

}
