package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.util.TypeSpec;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Apache HttpClient 5 的同步、框架中立 HTTP 实现。
 * <p>内部创建的客户端由本实例关闭；外部注入的客户端仍由原所有者管理。</p>
 */
public class ApacheHttpClientExchanger extends AbstractHttpExchanger<CloseableHttpClient> implements Closeable {

    public static final ApacheHttpClientExchanger INSTANCE =
            new ApacheHttpClientExchanger(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, null, true);

    private final CloseableHttpClient externalClient;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final CookieStore cookieStore;
    private final boolean shared;
    private volatile boolean closed;

    public static ApacheHttpClientExchanger create() {
        return new ApacheHttpClientExchanger();
    }

    public static ApacheHttpClientExchanger create(Duration connectTimeout, Duration readTimeout) {
        return new ApacheHttpClientExchanger(connectTimeout, readTimeout);
    }

    public static ApacheHttpClientExchanger create(CloseableHttpClient client) {
        return new ApacheHttpClientExchanger(client);
    }

    public static StatefulApacheHttpClientExchanger stateful() {
        return new StatefulApacheHttpClientExchanger();
    }

    public static StatefulApacheHttpClientExchanger stateful(Duration connectTimeout, Duration readTimeout) {
        return new StatefulApacheHttpClientExchanger(connectTimeout, readTimeout);
    }

    public ApacheHttpClientExchanger() {
        this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public ApacheHttpClientExchanger(Duration connectTimeout, Duration readTimeout) {
        this(connectTimeout, readTimeout, null, false);
    }

    public ApacheHttpClientExchanger(CloseableHttpClient client) {
        this.externalClient = Objects.requireNonNull(client, "client must not be null");
        this.connectTimeout = null;
        this.readTimeout = null;
        this.cookieStore = null;
        this.shared = false;
    }

    protected ApacheHttpClientExchanger(Duration connectTimeout, Duration readTimeout,
                                        CookieStore cookieStore, boolean shared) {
        this.connectTimeout = Objects.requireNonNull(connectTimeout, "connectTimeout must not be null");
        this.readTimeout = Objects.requireNonNull(readTimeout, "readTimeout must not be null");
        toMillis(connectTimeout, "connectTimeout");
        toMillis(readTimeout, "readTimeout");
        this.externalClient = null;
        this.cookieStore = cookieStore;
        this.shared = shared;
    }

    @Override
    public synchronized CloseableHttpClient getRequestInstance() {
        if (this.closed) {
            throw new IllegalStateException("HTTP客户端已关闭");
        }
        return super.getRequestInstance();
    }

    @Override
    protected CloseableHttpClient createRequestInstance() {
        if (this.externalClient != null) {
            return this.externalClient;
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(toMillis(this.connectTimeout, "connectTimeout"), TimeUnit.MILLISECONDS)
                .setResponseTimeout(toMillis(this.readTimeout, "readTimeout"), TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                .setMaxRedirects(10)
                .setCircularRedirectsAllowed(false)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new SafeRedirectStrategy())
                .disableAutomaticRetries();
        if (this.cookieStore == null) {
            builder.disableCookieManagement();
        } else {
            builder.setDefaultCookieStore(this.cookieStore);
        }
        return builder.build();
    }

    @Override
    public <T> HttpResponseEntity<T> request(CloseableHttpClient client, HttpRequest<?> request,
                                             TypeSpec<T> responseType) throws IOException {
        return this.execute(client, this.createApacheRequest(request), request, responseType);
    }

    @Override
    public <T> HttpResponseEntity<T> requestWithBody(CloseableHttpClient client, HttpBodyRequest request,
                                                     TypeSpec<T> responseType) throws IOException {
        HttpUriRequestBase apacheRequest = this.createApacheRequest(request);
        HttpEntity entity = this.createEntity(request);
        if (entity != null) {
            apacheRequest.setEntity(entity);
        }
        return this.execute(client, apacheRequest, request, responseType);
    }

    private HttpUriRequestBase createApacheRequest(HttpRequest<?> request) {
        HttpUriRequestBase apacheRequest = new HttpUriRequestBase(request.getMethod().getName(), request.getUri());
        request.getHeaders().forEach((name, values) -> {
            for (String value : values) {
                apacheRequest.addHeader(name, value);
            }
        });
        return apacheRequest;
    }

    protected HttpEntity createEntity(HttpBodyRequest request) throws IOException {
        Object body = request.getBody();
        if (body == null) {
            return null;
        }
        if (body instanceof HttpBodyEntity) {
            if (isFormUrlEncoded(request.getHeaders())) {
                return this.createFormEntity((HttpBodyEntity) body);
            }
            return this.createMultipartEntity((HttpBodyEntity) body);
        }
        ContentType contentType = resolveContentType(request.getHeaders().getFirst("Content-Type"));
        if (body instanceof byte[]) {
            return new ByteArrayEntity((byte[]) body,
                    contentType == null ? ContentType.APPLICATION_OCTET_STREAM : contentType);
        }
        if (body instanceof FileParam) {
            FileParam file = (FileParam) body;
            return new ByteArrayEntity(file.getBytes(), resolveFileContentType(file.getContentType()));
        }
        if (body instanceof String) {
            return new StringEntity((String) body, resolveCharset(request.getHeaders()));
        }
        String json = request.getJsonSerializer().serialize(body);
        return new StringEntity(json, contentType == null
                ? ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8) : contentType);
    }

    private HttpEntity createFormEntity(HttpBodyEntity body) {
        List<BasicNameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, List<Object>> entry : body.entrySet()) {
            for (Object value : entry.getValue()) {
                if (value instanceof FileParam) {
                    throw new IllegalArgumentException(
                            "application/x-www-form-urlencoded 表单不支持文件参数，请改用 multipart/form-data：" + entry.getKey());
                }
                parameters.add(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
            }
        }
        return new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8);
    }

    private HttpEntity createMultipartEntity(HttpBodyEntity body) throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setCharset(StandardCharsets.UTF_8);
        for (Map.Entry<String, List<Object>> entry : body.entrySet()) {
            for (Object value : entry.getValue()) {
                if (value instanceof FileParam) {
                    FileParam file = (FileParam) value;
                    String filename = file.getFilename();
                    if (filename == null || filename.trim().isEmpty()) {
                        throw new IllegalArgumentException("上传的文件名称不能为空");
                    }
                    builder.addBinaryBody(entry.getKey(), file.getBytes(),
                            resolveFileContentType(file.getContentType()), filename);
                } else {
                    builder.addTextBody(entry.getKey(), String.valueOf(value),
                            ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
                }
            }
        }
        return builder.build();
    }

    private <T> HttpResponseEntity<T> execute(CloseableHttpClient client, ClassicHttpRequest apacheRequest,
                                               HttpRequest<?> request, TypeSpec<T> responseType) throws IOException {
        return client.execute(apacheRequest, response -> {
            HttpHeaders headers = new HttpHeaders();
            for (Header header : response.getHeaders()) {
                headers.add(header.getName(), header.getValue());
            }
            byte[] bytes = response.getEntity() == null ? null : EntityUtils.toByteArray(response.getEntity());
            return this.convertResponse(request, response.getCode(), headers, bytes, responseType);
        });
    }

    @Override
    public synchronized void close() throws IOException {
        if (this.shared) {
            throw new IllegalStateException("共享默认HTTP客户端不能由单个调用方关闭，请使用create()创建独立实例");
        }
        if (this.closed) {
            return;
        }
        this.closed = true;
        CloseableHttpClient client = this.getInitializedRequestInstance();
        if (this.externalClient == null && client != null) {
            client.close();
        }
    }

    private static long toMillis(Duration duration, String name) {
        try {
            long millis = duration.toMillis();
            if (millis < 1) {
                throw new IllegalArgumentException(name + "必须为至少1毫秒的正时长");
            }
            return millis;
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(name + "换算毫秒时溢出", e);
        }
    }

    private static boolean isFormUrlEncoded(HttpHeaders headers) {
        String contentType = headers.getFirst("Content-Type");
        return contentType != null && contentType.trim().toLowerCase(Locale.ROOT)
                .startsWith(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
    }

    private static Charset resolveCharset(HttpHeaders headers) {
        ContentType contentType = resolveContentType(headers.getFirst("Content-Type"));
        return contentType != null && contentType.getCharset() != null
                ? contentType.getCharset() : StandardCharsets.UTF_8;
    }

    private static ContentType resolveContentType(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return ContentType.parse(value);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static ContentType resolveFileContentType(String value) {
        ContentType contentType = resolveContentType(value);
        return contentType == null ? ContentType.DEFAULT_BINARY : contentType;
    }

    /**
     * 在默认重定向策略上补充代理凭证和 HTTPS 降级保护。
     */
    protected static class SafeRedirectStrategy extends DefaultRedirectStrategy {
        @Override
        public boolean isRedirectAllowed(HttpHost currentTarget, HttpHost newTarget,
                                         org.apache.hc.core5.http.HttpRequest redirect, HttpContext context) {
            if (currentTarget != null && newTarget != null
                    && "https".equalsIgnoreCase(currentTarget.getSchemeName())
                    && "http".equalsIgnoreCase(newTarget.getSchemeName())) {
                return false;
            }
            if (!super.isRedirectAllowed(currentTarget, newTarget, redirect, context)) {
                return false;
            }
            if (currentTarget != null && newTarget != null && !currentTarget.equals(newTarget)
                    && redirect.containsHeader("Proxy-Authorization")) {
                return false;
            }
            return true;
        }
    }
}
