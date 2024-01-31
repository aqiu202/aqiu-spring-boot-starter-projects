package com.github.aqiu202.util.scan.filter;

import com.github.aqiu202.util.ReflectionUtils;

import java.lang.annotation.Annotation;

/**
 * class注解过滤器
 */
public interface ClassAnnotationFilter extends ClassFilter {
    @Override
    default boolean matches(Class<?> param) {
        return ReflectionUtils.hasAnnotation(param, this.getAnnotationType());
    }

    Class<? extends Annotation> getAnnotationType();

}
