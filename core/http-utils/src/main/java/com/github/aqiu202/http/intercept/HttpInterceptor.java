package com.github.aqiu202.http.intercept;

import com.github.aqiu202.http.HttpRequest;

@FunctionalInterface
public interface HttpInterceptor {

    int DEFAULT_ORDER = 0;

    int DEFAULT_CONFIGURE_ORDER = 3000;

    void intercept(InterceptorContext context, HttpRequest<?> request);

    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
