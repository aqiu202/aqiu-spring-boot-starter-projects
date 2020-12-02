package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * <pre>AbstractAnnotationPointcut</pre>
 *
 * @author aqiu 2020/11/29 16:15
 **/
public abstract class AbstractAnnotationPointcut<T extends Annotation> extends StaticMethodMatcherPointcut {

    protected final Class<T> annotationType;

    protected AbstractAnnotationPointcut(Class<T> annotationType) {
        this.annotationType = annotationType;
    }

    protected Class<T> getAnnotationType() {
        return annotationType;
    }
}
