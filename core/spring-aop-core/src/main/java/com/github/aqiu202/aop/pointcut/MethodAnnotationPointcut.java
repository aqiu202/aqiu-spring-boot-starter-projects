package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * <pre>MethodAnnotationPointcut</pre>
 *
 * @author aqiu 2020/11/29 12:12
 **/
public class MethodAnnotationPointcut<T extends Annotation> extends
        AbstractAnnotationPointcut<T> {

    public MethodAnnotationPointcut(Class<T> annotationType) {
        super(annotationType);
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return AnnotatedElementUtils.isAnnotated(method, this.annotationType);
//        return AnnotationUtils.getAnnotation(method, this.annotationType) != null;
    }
}
