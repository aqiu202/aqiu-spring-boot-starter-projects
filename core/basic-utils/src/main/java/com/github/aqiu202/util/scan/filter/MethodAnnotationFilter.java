package com.github.aqiu202.util.scan.filter;

import com.github.aqiu202.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 类成员方法上注解过滤器
 */
public interface MethodAnnotationFilter extends ClassFilter {

    @Override
    default boolean matches(Class<?> param) {
        return ReflectionUtils.getAllMethod(param).stream()
                .anyMatch(this::methodMatches);
    }

    default boolean methodMatches(Method method) {
        return ReflectionUtils.hasAnnotation(method, this.getAnnotationType());
    }

    Class<? extends Annotation> getAnnotationType();

}
