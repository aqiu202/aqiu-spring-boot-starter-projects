package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * <pre>ClassAnnotationPointcut</pre>
 *
 * @author aqiu 2020/11/29 16:14
 **/
public class ClassAnnotationPointcut<T extends Annotation> extends
        AbstractAnnotationPointcut<T> implements Pointcut {

    public ClassAnnotationPointcut(Class<T> annotationType) {
        super(annotationType);
    }

    @Override
    public ClassFilter getClassFilter() {
        return new AnnotationClassFilter(this.annotationType, this.checkInherited);
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        Predicate<Class<T>> assertFun = this.isCheckInherited() ? type -> AnnotatedElementUtils.hasAnnotation(aClass, type) : aClass::isAnnotationPresent;
        return assertFun.test(this.getAnnotationType());
    }

}
