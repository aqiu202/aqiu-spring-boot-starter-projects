package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotatedElementUtils;

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
        return AnnotatedElementUtils.isAnnotated(aClass, this.annotationType) ||
                AnnotatedElementUtils.isAnnotated(method, this.annotationType);
//        return AnnotationUtils.getAnnotation(aClass, this.annotationType) != null ||
//                AnnotationUtils.getAnnotation(method, this.annotationType) != null;
    }
}
