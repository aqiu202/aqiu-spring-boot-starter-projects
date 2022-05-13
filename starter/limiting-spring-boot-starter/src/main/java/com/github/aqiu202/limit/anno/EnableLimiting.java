package com.github.aqiu202.limit.anno;

import com.github.aqiu202.cache.anno.EnableTtlCaching;
import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.limit.config.LimitingConfigurationSelector;
import com.github.aqiu202.limit.config.LimitingKeyGeneratorConfiguration;
import com.github.aqiu202.limit.config.TtlCacheLockConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableTtlCaching
@Import({TtlCacheLockConfiguration.class,
        LimitingKeyGeneratorConfiguration.class,
        LimitingConfigurationSelector.class})
public @interface EnableLimiting {

    AdviceMode mode() default AdviceMode.PROXY;

    @AliasFor(annotation = EnableTtlCaching.class)
    CacheMode cacheMode() default CacheMode.caffeine;

    /**
     * 多少秒内每个用户只允许访问一次
     *
     * @return timeout
     */
    @AliasFor(annotation = EnableTtlCaching.class)
    long timeout() default 3;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    @AliasFor(annotation = EnableTtlCaching.class)
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
