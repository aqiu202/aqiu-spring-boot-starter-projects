package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>{@link SimpleAnnotationInterceptor}</pre>
 *
 * @author aqiu 2020/12/13 20:35
 **/
public class SimpleAnnotationInterceptor<T extends Annotation> implements
        AnnotationMethodInterceptor<T> {

    private static final Logger log = LoggerFactory.getLogger(SimpleAnnotationInterceptor.class);

    @Override
    public Object intercept(MethodInvocation invocation, T t) throws Throwable {
        this.beforeIntercept(invocation, t);
        Throwable throwable = null;
        try {
            Object result = this.doIntercept(invocation, t);
            this.afterReturning(invocation, t, result);
            return result;
        } catch (Throwable th) {
            log.error("", th);
            throwable = th;
            return this.onError(invocation, t, th);
        } finally {
            this.afterIntercept(invocation, t, throwable);
        }
    }

    protected void afterReturning(MethodInvocation invocation, T t, Object result) {
    }

    protected void beforeIntercept(MethodInvocation invocation, T t) {

    }

    protected Object doIntercept(MethodInvocation invocation, T t) throws Throwable {
        return invocation.proceed();
    }

    protected Object onError(MethodInvocation invocation, T t, Throwable throwable) throws Throwable {
        throw throwable;
    }

    protected void afterIntercept(MethodInvocation invocation, T t, @Nullable Throwable throwable) {

    }

}
