package com.github.aqiu202.qlock.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AliasFor;

/**
 * <pre>QLock</pre>
 *
 * @author aqiu 2020/12/2 13:11
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface QLock {

    @AliasFor("key")
    String value() default "";

    String message() default "请不要重复提交";

    String keyGenerator() default "";

    /**
     * 几秒内不允许重复访问
     * @return timeout
     */
    long timeout() default 0;

    /**
     * 时间单位
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * key设置
     * @return key
     */
    @AliasFor("value")
    String key() default "";
}
