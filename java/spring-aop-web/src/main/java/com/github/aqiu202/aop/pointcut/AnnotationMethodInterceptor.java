package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>AnnotationMethodInterceptor</pre>
 *
 * @author aqiu 2020/11/29 23:52
 **/
@FunctionalInterface
public interface AnnotationMethodInterceptor<T extends Annotation> {

    Object intercept(MethodInvocation invocation, T t) throws Throwable;
}
