package com.github.aqiu202.http.intercept;

import com.github.aqiu202.http.HttpRequest;

@FunctionalInterface
public interface HttpInterceptor {

    void intercept(InterceptorContext context, HttpRequest<?> request);

}
