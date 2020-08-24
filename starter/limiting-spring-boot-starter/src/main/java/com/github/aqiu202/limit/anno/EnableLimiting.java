package com.github.aqiu202.limit.anno;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.aqiu202.cache.anno.EnableTimedCaching;
import com.github.aqiu202.cache.anno.EnableTimedCaching.CacheMode;
import com.github.aqiu202.limit.config.RepeatLimitConfig;
import com.github.aqiu202.limit.config.TimedCacheLockConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableTimedCaching
@Import({TimedCacheLockConfiguration.class, RepeatLimitConfig.class})
public @interface EnableLimiting {

    @AliasFor(annotation = EnableTimedCaching.class)
    CacheMode cacheMode() default CacheMode.caffeine;

    /**
     * 多少秒内每个用户只允许访问一次
     * @return timeout
     */
    @AliasFor(annotation = EnableTimedCaching.class)
    long timeout() default 3;

    /**
     * 时间单位
     * @return 时间单位
     */
    @AliasFor(annotation = EnableTimedCaching.class)
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
