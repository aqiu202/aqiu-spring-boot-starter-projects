package com.github.aqiu202.cache.anno;

import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;
import com.github.aqiu202.ttl.data.str.StringGuavaCache;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
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
@Import(TtlCacheConfigRegistrar.class)
public @interface EnableTtlCaching {

    enum CacheMode {

        redis(StringRedisCache.class, true),
        guava(StringGuavaCache.class, false),
        caffeine(StringCaffeineCache.class, false),
        none(null, false);

        private final Class<? extends StringTtlCache> clazz;
        private final boolean autowireCandidate;

        CacheMode(Class<? extends StringTtlCache> clazz, boolean autowireCandidate) {
            this.clazz = clazz;
            this.autowireCandidate = autowireCandidate;
        }

        public Class<?> getValue() {
            return this.clazz;
        }

        public boolean isAutowireCandidate() {
            return this.autowireCandidate;
        }
    }


    CacheMode cacheMode() default CacheMode.caffeine;

    /**
     * 缓存超时时间
     *
     * @return 缓存超时时间
     */
    long timeout() default 3600;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
