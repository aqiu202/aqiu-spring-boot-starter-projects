package com.github.aqiu202.ttl.data.impl;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;

public class RedisCache<K, V> extends AbstractTtlCache<K, V> implements InitializingBean {

    private RedisTemplate<K, V> cache;

    public RedisCache() {
    }

    public void setCache(RedisTemplate<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void set(@Nonnull K key, @Nonnull V value, long expired, @Nonnull TimeUnit unit) {
        this.cache.opsForValue().set(key, value, Expiration.from(expired, unit));
    }

    @Override
    public V get(@Nonnull K key) {
        return this.cache.opsForValue().get(key);
    }

    @Override
    public Boolean exists(@Nonnull K key) {
        return this.cache.hasKey(key);
    }

    @Override
    public Boolean setIfAbsent(@Nonnull K key, @Nonnull V value, long expired, @Nonnull TimeUnit unit) {
        return this.cache.opsForValue().setIfAbsent(key, value, Expiration.from(expired, unit));
    }

    @Override
    public Boolean delete(@Nonnull K key) {
        return this.cache.delete(key);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.cache, "RedisTemplate不能为空");
    }
}
