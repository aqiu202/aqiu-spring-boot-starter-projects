package com.github.aqiu202.qlock.anno;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.lock.base.LockValueStrategyMode;
import com.github.aqiu202.lock.base.ReentrantCacheKeyLock;
import com.github.aqiu202.lock.cache.SimpleCacheKeyLock;
import com.github.aqiu202.lock.redisson.RedissonKeyLock;
import com.github.aqiu202.lock.redisson.ReentrantRedissonKeyLock;
import com.github.aqiu202.lock.zk.ReentrantZookeeperKeyLock;
import com.github.aqiu202.lock.zk.ZookeeperKeyLock;
import com.github.aqiu202.qlock.config.QLockAutoConfiguration;
import com.github.aqiu202.qlock.config.QLockConfigRegistrar;
import com.github.aqiu202.qlock.config.QLockZkCuratorSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <pre>EnableQLock</pre>
 *
 * @author aqiu 2020/12/2 13:16
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({QLockZkCuratorSelector.class,
        QLockConfigRegistrar.class,
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

    LockValueStrategyMode lockValueStrategyMode() default LockValueStrategyMode.thread;

    enum LockMode {
        guava(CacheMode.guava, SimpleCacheKeyLock.class),
        guava_reentrant(CacheMode.guava, ReentrantCacheKeyLock.class, true),
        caffeine(CacheMode.caffeine, SimpleCacheKeyLock.class),
        caffeine_reentrant(CacheMode.caffeine, ReentrantCacheKeyLock.class, true),
        redis(CacheMode.redis, SimpleCacheKeyLock.class),
        redis_reentrant(CacheMode.redis, ReentrantCacheKeyLock.class, true),
        zookeeper(CacheMode.none, ZookeeperKeyLock.class),
        zookeeper_reentrant(CacheMode.none, ReentrantZookeeperKeyLock.class),
        redisson(CacheMode.none, RedissonKeyLock.class),
        redisson_reentrant(CacheMode.none, ReentrantRedissonKeyLock.class);

        private final CacheMode cacheMode;
        private final Class<? extends KeyLock> lockClass;
        private final boolean hasIdGenerator;

        LockMode(CacheMode cacheMode, Class<? extends KeyLock> lockClass) {
            this(cacheMode, lockClass, false);
        }

        LockMode(CacheMode cacheMode, Class<? extends KeyLock> lockClass, boolean hasIdGenerator) {
            this.cacheMode = cacheMode;
            this.lockClass = lockClass;
            this.hasIdGenerator = hasIdGenerator;
        }

        public CacheMode getCacheMode() {
            return cacheMode;
        }

        public Class<? extends KeyLock> getLockClass() {
            return lockClass;
        }

        public boolean isHasIdGenerator() {
            return hasIdGenerator;
        }

    }

    /**
     * 是否另外注册缓存
     *
     * @return false (默认)使用ttl-cache-spring-boot-starter内置缓存 true 重新注册ttl-cache缓存
     */
    boolean otherCaching() default false;

    IdType idType() default IdType.AUTO;

}
