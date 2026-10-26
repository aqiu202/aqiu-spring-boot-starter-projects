package com.github.aqiu202.http.exchange;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

/**
 * 有状态的 {@link RestClientExchanger}。
 * <p>
 * 每个实例内部维护独立的 {@link CookieStore}，在通过身份认证接口拿到 Session/Cookie 之后，
 * 使用同一实例发起的后续请求会自动带上这些认证凭据，从而完成有状态的会话调用。
 * <p>
 * 底层基于 JDK 自带的 {@link HttpClient}，通过 {@link CookieManager} 完成 Cookie 的
 * 自动收集与回填，并通过 {@link JdkClientHttpRequestFactory} 装配到 {@link RestClient}，
 * 实例之间互不影响。
 */
public class StatefulRestClientExchanger extends RestClientExchanger {

    public static final RestClientExchanger INSTANCE = new StatefulRestClientExchanger();

    private final HttpClient httpClient;
    private final CookieManager cookieManager;

    public StatefulRestClientExchanger() {
        this(DEFAULT_CONNECT_TIMEOUT);
    }

    public StatefulRestClientExchanger(Duration connectTimeout) {
        // 使用内存型 CookieStore，作用范围仅限当前实例
        this(new CookieManager(null, CookiePolicy.ACCEPT_ALL), connectTimeout);
    }

    private StatefulRestClientExchanger(CookieManager cookieManager, Duration connectTimeout) {
        this(cookieManager, defaultHttpClientBuilder(connectTimeout).cookieHandler(cookieManager).build());
    }

    private StatefulRestClientExchanger(CookieManager cookieManager, HttpClient httpClient) {
        // 将带 CookieHandler 的 JDK HttpClient 装配到 RestClient，直接交给父类，避免额外创建默认客户端
        super(defaultRequestFactory(httpClient, DEFAULT_READ_TIMEOUT));
        this.cookieManager = cookieManager;
        this.httpClient = httpClient;
    }

    /**
     * 获取当前实例内部维护的 CookieStore，可用于查看、添加、清理 Cookie。
     */
    public CookieStore getCookieStore() {
        return this.cookieManager.getCookieStore();
    }

    /**
     * 查看指定 URI 下会随请求发送的 Cookie 列表。
     */
    public List<HttpCookie> getCookies(URI uri) {
        return this.cookieManager.getCookieStore().get(uri);
    }

    /**
     * 手动添加 Cookie，例如需要从外部持久化中恢复会话时使用。
     */
    public void addCookie(URI uri, HttpCookie cookie) {
        this.cookieManager.getCookieStore().add(uri, cookie);
    }

    /**
     * 清空所有 Cookie，可用于登出或重置会话。
     */
    public void clearCookies() {
        this.cookieManager.getCookieStore().removeAll();
    }

    /**
     * 判断当前实例下指定 URI 是否已有 Cookie，简易的会话是否已建立的判断。
     */
    public boolean hasCookies(URI uri) {
        List<HttpCookie> cookies = this.cookieManager.getCookieStore().get(uri);
        return cookies != null && !cookies.isEmpty();
    }

    /**
     * 获取底层的 JDK {@link HttpClient}，便于需要时进行更底层的定制。
     */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * 获取内部构建好的 {@link RestClient}，可直接用于常规调用。
     */
    public RestClient getRestClient() {
        return this.getRequestInstance();
    }
}
