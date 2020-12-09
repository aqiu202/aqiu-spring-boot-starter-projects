package com.github.aqiu202.starters.jpa.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.dao.OptimisticLockingFailureException;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Retry
public @interface OptimisticLockRetry {

    @AliasFor(annotation = Retry.class)
    Class<? extends Throwable>[] value() default {OptimisticLockingFailureException.class};

    @AliasFor(annotation = Retry.class)
    Class<? extends Throwable>[] exclude() default {};

    @AliasFor(annotation = Retry.class)
    int times() default 3;

}
