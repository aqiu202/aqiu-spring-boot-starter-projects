package com.github.aqiu202.cache.anno;

import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.TtlCache;
import com.github.aqiu202.ttl.data.impl.CaffeineCache;
import com.github.aqiu202.ttl.data.impl.RedisCache;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TtlCacheConfigRegistrar.class)
public @interface EnableTtlCaching {

    enum CacheMode {

        redis(RedisCache.class, StringRedisCache.class),
        caffeine(CaffeineCache.class, StringCaffeineCache.class),
        none(null, null);

        private final Class<? extends StringTtlCache> stringCacheClass;
        private final Class<? extends TtlCache> cacheClass;

        CacheMode(Class<? extends TtlCache> cacheClass,
                Class<? extends StringTtlCache> stringCacheClass) {
            this.cacheClass = cacheClass;
            this.stringCacheClass = stringCacheClass;
        }

        public Class<? extends StringTtlCache> getStringCacheClass() {
            return this.stringCacheClass;
        }

        public Class<? extends TtlCache> getCacheClass() {
            return this.cacheClass;
        }

    }

    @AliasFor("cacheMode")
    CacheMode value() default CacheMode.caffeine;

    @AliasFor("value")
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
