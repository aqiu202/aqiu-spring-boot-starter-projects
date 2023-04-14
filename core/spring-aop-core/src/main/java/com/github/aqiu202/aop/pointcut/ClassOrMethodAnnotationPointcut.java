package com.github.aqiu202.aop.pointcut;

import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.function.BiPredicate;

/**
 * <pre>ClassOrMethodAnnotationPointcut</pre>
 *
 * @author aqiu 2020/11/29 12:12
 **/
public class ClassOrMethodAnnotationPointcut<T extends Annotation> extends
        AbstractAnnotationPointcut<T> {

    public ClassOrMethodAnnotationPointcut(Class<T> annotationType) {
        super(annotationType);
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        BiPredicate<AnnotatedElement, Class<T>> assertFun = this.isCheckInherited() ?
                AnnotatedElementUtils::hasAnnotation : AnnotatedElement::isAnnotationPresent;
        return assertFun.test(aClass, this.annotationType) ||
                assertFun.test(method, this.annotationType);
    }
}
