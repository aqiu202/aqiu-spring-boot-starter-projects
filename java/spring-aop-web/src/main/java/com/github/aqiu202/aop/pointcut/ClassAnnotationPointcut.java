package com.github.aqiu202.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;

/**
 * <pre>ClassAnnotationPointcut</pre>
 *
 * @author aqiu 2020/11/29 16:14
 **/
public class ClassAnnotationPointcut<T extends Annotation> extends
        AbstractAnnotationPointcut<T> implements Pointcut {

    private boolean checkInherited;

    public ClassAnnotationPointcut(Class<T> annotationType) {
        super(annotationType);
    }

    @Override
    public ClassFilter getClassFilter() {
        return new AnnotationClassFilter(this.annotationType, this.checkInherited);
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return true;
    }

    public boolean isCheckInherited() {
        return checkInherited;
    }

    public void setCheckInherited(boolean checkInherited) {
        this.checkInherited = checkInherited;
    }
}
