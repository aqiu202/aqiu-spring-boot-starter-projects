package com.github.aqiu202.cache.anno;

import com.github.aqiu202.cache.config.TimedCacheConfigRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TimedCacheConfigRegistrar.class)
public @interface EnableTimedCaching {

    enum CacheMode {
        redis, guava, caffeine
    }

    CacheMode cacheMode() default CacheMode.caffeine;

    /**
     * 缓存超时时间
     * @return 缓存超时时间
     */
    long timeout() default 3600;

    /**
     * 时间单位
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
