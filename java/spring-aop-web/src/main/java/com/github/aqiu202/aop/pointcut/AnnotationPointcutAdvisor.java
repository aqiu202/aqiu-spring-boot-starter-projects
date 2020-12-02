package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;

/**
 * <pre>AnnotationPointcutAdvisor</pre>
 *
 * @author aqiu 2020/11/29 16:38
 **/
public class AnnotationPointcutAdvisor<T extends Annotation> extends AbstractPointcutAdvisor {

    private final AbstractAnnotationPointcut<T> pointcut;
    private final AnnotationMethodInterceptor<T> interceptor;

    public AnnotationPointcutAdvisor(AbstractAnnotationPointcut<T> pointcut,
            AnnotationMethodInterceptor<T> interceptor) {
        this.pointcut = pointcut;
        this.interceptor = interceptor;
    }

    public AnnotationPointcutAdvisor(Class<T> annotationType,
            AnnotationMethodInterceptor<T> interceptor) {
        this(new ClassOrMethodAnnotationPointcut<>(annotationType), interceptor);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return (MethodInterceptor) methodInvocation -> this.interceptor
                .intercept(methodInvocation, this.getAnnotation(methodInvocation));
    }

    private T getAnnotation(MethodInvocation methodInvocation) {
        final Class<?> aClass = methodInvocation.getThis().getClass();
        final Method method = methodInvocation.getMethod();
        final Class<T> annotationType = this.pointcut.getAnnotationType();
        return this
                .mergeAnnotation(AnnotatedElementUtils.getMergedAnnotation(aClass, annotationType),
                        AnnotatedElementUtils.getMergedAnnotation(method, annotationType));
    }

    public T mergeAnnotation(@Nullable T classAnnotation, @Nullable T methodAnnotation) {
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        return classAnnotation;
    }
}
