package com.github.aqiu202.qlock.anno;

import com.github.aqiu202.cache.anno.EnableTtlCaching;
import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;
import com.github.aqiu202.ttl.data.str.StringGuavaCache;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AliasFor;

/**
 * <pre>EnableQLock</pre>
 *
 * @author aqiu 2020/12/2 13:16
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableQLock {

    @AliasFor("lockMode")
    LockMode value() default LockMode.caffeine;

    @AliasFor("value")
    LockMode lockMode() default LockMode.caffeine;

    /**
     * 该时间后自动释放锁（zookeeper模式重试最大时长）
     * @return timeout
     */
    long timeout() default 5;

    /**
     * 时间单位
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    enum LockMode {
        guava,
        caffeine,
        redis,
        zookeeper;
    }

}
