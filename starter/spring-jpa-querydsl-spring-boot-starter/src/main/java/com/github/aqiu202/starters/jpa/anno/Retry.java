package com.github.aqiu202.starters.jpa.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Retry {

    Class<? extends Throwable>[] value() default {};

    Class<? extends Throwable>[] exclude() default {};

    int times() default 3;

}
