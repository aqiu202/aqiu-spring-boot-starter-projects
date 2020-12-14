package com.github.aqiu202.limit.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.aqiu202.aop.keygen.KeyGenerator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface ThreadLimiting {

    @AliasFor("key")
    String value() default "";

    /**
     * 可允许的最大访问线程数量
     * @return 最大并发数
     */
    int threads() default 200;

    String message() default "服务器繁忙，请稍后再试";

    String keyGenerator() default "";

    /**
     * key设置
     * @return key
     */
    @AliasFor("value")
    String key() default "";

}
