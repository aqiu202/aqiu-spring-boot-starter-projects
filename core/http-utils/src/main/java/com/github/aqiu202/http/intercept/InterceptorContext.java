package com.github.aqiu202.http.intercept;

/**
 * TODO 上下文内容需要补充
 */
public class InterceptorContext {

    protected InterceptorContext() {
    }

    public static InterceptorContext fromThreadContext() {
        return new InterceptorContext();
    }
}
