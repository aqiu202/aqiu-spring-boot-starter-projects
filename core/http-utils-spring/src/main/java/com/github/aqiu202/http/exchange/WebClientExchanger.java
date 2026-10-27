package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.HttpService;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

public class WebClientExchanger extends AbstractSpringHttpExchanger<WebClient> {

    public static final WebClientExchanger INSTANCE = new WebClientExchanger();

    public static WebClientExchanger stateful() {
        return new StatefulWebClientExchanger();
    }

    public static WebClientExchanger stateful(Duration timeout) {
        return new StatefulWebClientExchanger(timeout);
    }

    public static WebClientExchanger create() {
        return new WebClientExchanger();
    }

    public static WebClientExchanger create(WebClient webClient) {
        return new WebClientExchanger(webClient);
    }

    public static WebClientExchanger create(Duration connectTimeout, Duration readTimeout) {
        return new WebClientExchanger(connectTimeout, readTimeout);
    }

    /**
     * 外部传入的客户端；为空时按超时配置惰性创建默认客户端
     */
    private final WebClient webClient;
    private final Duration connectTimeout;
    private final Duration readTimeout;

    /**
     * 默认连接超时 {@link #DEFAULT_CONNECT_TIMEOUT}、读取超时 {@link #DEFAULT_READ_TIMEOUT}，并自动跟随重定向。
     */
    public WebClientExchanger() {
        this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public WebClientExchanger(Duration connectTimeout, Duration readTimeout) {
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
        this.webClient = null;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public WebClientExchanger(WebClient webClient) {
        Assert.notNull(webClient, "webClient must not be null");
        this.webClient = webClient;
        this.connectTimeout = null;
        this.readTimeout = null;
    }

    @Override
    protected WebClient createRequestInstance() {
        if (this.webClient != null) {
            return this.webClient;
        }
        return buildWebClient(defaultHttpClientBuilder(this.connectTimeout).build(), this.readTimeout);
    }

    /**
     * 基于 JDK {@link HttpClient} 构建 {@link WebClient}，与其他实现使用相同的底层连接与重定向策略；
     * 同时取消响应体内存缓冲上限（默认 256KB），与 RestClient / RestTemplate 行为一致。
     */
    protected static WebClient buildWebClient(HttpClient httpClient, Duration readTimeout) {
        JdkClientHttpConnector connector = new JdkClientHttpConnector(httpClient);
        connector.setReadTimeout(readTimeout);
        return WebClient.builder()
                .clientConnector(connector)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
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
                        requestHeaders.getValues().forEach(headers::addAll);
                    }
                });
    }

    public void resolveRequestBody(WebClient.RequestBodySpec requestBodySpec, HttpBodyRequest request) throws IOException {
        Object processedBody = this.processBody(request);
        if (processedBody != null) {
            requestBodySpec.bodyValue(processedBody);
        }
    }

    public <T> HttpResponseEntity<T> forResponse(HttpRequest<?> request, WebClient.RequestBodySpec requestBodySpec,
                                                 TypeSpec<T> responseType) {
        ResponseEntity<byte[]> responseEntity = requestBodySpec
                .exchangeToMono(response -> response.body((inputMessage, context) ->
                                DataBufferUtils.join(inputMessage.getBody()))
                        .map(buffer -> {
                            byte[] bytes = new byte[buffer.readableByteCount()];
                            buffer.read(bytes);
                            DataBufferUtils.release(buffer);
                            return new ResponseEntity<>(bytes, response.headers().asHttpHeaders(),
                                    response.statusCode());
                        })
                        .defaultIfEmpty(new ResponseEntity<>(null, response.headers().asHttpHeaders(),
                                response.statusCode())))
                .block();
        return this.convertResponse(request, responseEntity, responseType);
    }

}
