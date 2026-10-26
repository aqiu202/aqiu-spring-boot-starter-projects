package com.github.aqiu202.http.util;

import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 响应感知的条件重试策略。
 * <p>
 * 判定发生在响应消费者之前：命中条件且未超过重试次数时，先执行 {@code beforeRetry}
 * （可用于刷新令牌、改写请求头），再重发同一请求；未命中或次数用尽后交回响应消费者。
 * <p>
 * 重发依赖请求体可重放，{@link com.github.aqiu202.http.HttpBodyRequest#jsonBody(Object)}
 * 保存的是序列化后的字符串，可以安全重发；流式请求体不具备该特性。
 */
public final class RetryPolicy {

    private final Predicate<HttpResponseEntity<?>> condition;

    private final int maxRetries;

    private final Consumer<HttpRequest<?>> beforeRetry;

    private RetryPolicy(Predicate<HttpResponseEntity<?>> condition, int maxRetries,
                        Consumer<HttpRequest<?>> beforeRetry) {
        this.condition = condition;
        this.maxRetries = maxRetries;
        this.beforeRetry = beforeRetry;
    }

    public static RetryPolicy of(Predicate<HttpResponseEntity<?>> condition, int maxRetries) {
        return of(condition, maxRetries, request -> {
        });
    }

    public static RetryPolicy of(Predicate<HttpResponseEntity<?>> condition, int maxRetries,
                                 Consumer<HttpRequest<?>> beforeRetry) {
        Objects.requireNonNull(condition, "condition must not be null");
        Objects.requireNonNull(beforeRetry, "beforeRetry must not be null");
        if (maxRetries < 1) {
            throw new IllegalArgumentException("maxRetries must be greater than 0");
        }
        return new RetryPolicy(condition, maxRetries, beforeRetry);
    }

    public boolean matches(HttpResponseEntity<?> response) {
        return this.condition.test(response);
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public void beforeRetry(HttpRequest<?> request) {
        this.beforeRetry.accept(request);
    }
}
