package com.github.aqiu202.qlock.anno;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.lock.base.Lock;
import com.github.aqiu202.lock.centralize.LocaleTtlLock;
import com.github.aqiu202.lock.centralize.ReentrantLocaleTtlLock;
import com.github.aqiu202.lock.distributed.RedisTtlLock;
import com.github.aqiu202.lock.distributed.ReentrantRedisTtlLock;
import com.github.aqiu202.lock.distributed.ZookeeperLock;
import com.github.aqiu202.qlock.config.QLockAutoConfiguration;
import com.github.aqiu202.qlock.config.QLockConfigRegistrar;
import com.github.aqiu202.qlock.config.TtlLockCacheConfigRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * <pre>EnableQLock</pre>
 *
 * @author aqiu 2020/12/2 13:16
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({TtlLockCacheConfigRegistrar.class, QLockConfigRegistrar.class,
        QLockAutoConfiguration.class})
public @interface EnableQLock {

    @AliasFor("lockMode")
    LockMode value() default LockMode.caffeine;

    @AliasFor("value")
    LockMode lockMode() default LockMode.caffeine;

    /**
     * 该时间后自动释放锁（zookeeper模式重试最大时长）
     *
     * @return timeout
     */
    long timeout() default 5;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    enum LockMode {
        guava(CacheMode.guava, LocaleTtlLock.class),
        guava_r(CacheMode.guava, ReentrantLocaleTtlLock.class),
        caffeine(CacheMode.caffeine, LocaleTtlLock.class),
        caffeine_r(CacheMode.caffeine, ReentrantLocaleTtlLock.class),
        redis(CacheMode.redis, RedisTtlLock.class),
        redis_r(CacheMode.redis, ReentrantRedisTtlLock.class),
        zookeeper(CacheMode.none, ZookeeperLock.class);

        private final CacheMode cacheMode;
        private final Class<? extends Lock> lockClass;

        LockMode(CacheMode cacheMode, Class<? extends Lock> lockClass) {
            this.cacheMode = cacheMode;
            this.lockClass = lockClass;
        }

        public CacheMode getCacheMode() {
            return cacheMode;
        }

        public Class<? extends Lock> getLockClass() {
            return lockClass;
        }
    }

}
