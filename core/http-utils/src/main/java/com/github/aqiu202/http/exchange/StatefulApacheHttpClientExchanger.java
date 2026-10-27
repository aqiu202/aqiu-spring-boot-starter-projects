package com.github.aqiu202.http.exchange;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * 每个实例持有独立 CookieStore 的 Apache HTTP 客户端。
 */
public class StatefulApacheHttpClientExchanger extends ApacheHttpClientExchanger {

    private final BasicCookieStore cookies;

    public StatefulApacheHttpClientExchanger() {
        this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public StatefulApacheHttpClientExchanger(Duration connectTimeout, Duration readTimeout) {
        this(connectTimeout, readTimeout, new BasicCookieStore());
    }

    private StatefulApacheHttpClientExchanger(Duration connectTimeout, Duration readTimeout,
                                              BasicCookieStore cookies) {
        super(connectTimeout, readTimeout, cookies, false);
        this.cookies = cookies;
    }

    public CookieStore getCookieStore() {
        return this.cookies;
    }

    public List<Cookie> getCookies() {
        return this.cookies.getCookies();
    }

    public void addCookie(Cookie cookie) {
        this.cookies.addCookie(Objects.requireNonNull(cookie, "cookie must not be null"));
    }

    public void clearCookies() {
        this.cookies.clear();
    }
}
