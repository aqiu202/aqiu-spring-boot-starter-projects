package com.github.aqiu202.qlock.anno;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.lock.base.Lock;
import com.github.aqiu202.lock.base.LockValueHolderStrategy;
import com.github.aqiu202.lock.centralize.LocaleTtlLock;
import com.github.aqiu202.lock.centralize.ReentrantLocaleTtlLock;
import com.github.aqiu202.lock.distributed.RedisTtlLock;
import com.github.aqiu202.lock.distributed.ReentrantRedisTtlLock;
import com.github.aqiu202.lock.distributed.ZookeeperLock;
import com.github.aqiu202.qlock.config.QLockAutoConfiguration;
import com.github.aqiu202.qlock.config.QLockConfigRegistrar;
import com.github.aqiu202.qlock.config.QLockZkCuratorSelector;
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

    LockValueHolderStrategy lockValueHolderStrategy() default LockValueHolderStrategy.thread;

    enum LockMode {
        guava(CacheMode.guava, LocaleTtlLock.class),
        guava_r(CacheMode.guava, ReentrantLocaleTtlLock.class, true),
        caffeine(CacheMode.caffeine, LocaleTtlLock.class),
        caffeine_r(CacheMode.caffeine, ReentrantLocaleTtlLock.class, true),
        redis(CacheMode.redis, RedisTtlLock.class),
        redis_r(CacheMode.redis, ReentrantRedisTtlLock.class, true),
        zookeeper(CacheMode.none, ZookeeperLock.class);

        private final CacheMode cacheMode;
        private final Class<? extends Lock> lockClass;
        private final boolean hasIdGenerator;

        LockMode(CacheMode cacheMode, Class<? extends Lock> lockClass) {
            this(cacheMode, lockClass, false);
        }

        LockMode(CacheMode cacheMode, Class<? extends Lock> lockClass, boolean hasIdGenerator) {
            this.cacheMode = cacheMode;
            this.lockClass = lockClass;
            this.hasIdGenerator = hasIdGenerator;
        }

        public CacheMode getCacheMode() {
            return cacheMode;
        }

        public Class<? extends Lock> getLockClass() {
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
