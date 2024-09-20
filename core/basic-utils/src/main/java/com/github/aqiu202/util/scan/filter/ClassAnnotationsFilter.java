package com.github.aqiu202.util.scan.filter;

import com.github.aqiu202.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * class注解过滤器
 */
public interface ClassAnnotationsFilter extends ClassFilter {
    @Override
    default boolean matches(Class<?> param) {
        return this.getAnnotationTypes().stream()
                .anyMatch(type -> ReflectionUtils.hasAnnotation(param, type));
    }

    Collection<Class<? extends Annotation>> getAnnotationTypes();

}
