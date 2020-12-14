package com.github.aqiu202.limit.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.aqiu202.limit.config.LimitingConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AliasFor;


@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface RepeatLimiting {

    @AliasFor("key")
    String value() default "";

    String message() default "请不要重复提交";

    String keyGenerator() default LimitingConfiguration.SESSION_METHOD_KEY_GENERATOR_NAME;

    /**
     * 几秒内不允许重复访问
     *
     * @return timeout
     */
    long timeout() default 0;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * key设置
     *
     * @return key
     */
    @AliasFor("value")
    String key() default "";

}
