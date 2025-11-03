package com.github.aqiu202.ttl.data.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.Policy.VarExpiration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.lang.NonNull;

public class CaffeineCache<K, V> extends AbstractTtlCache<K, V> {

    private volatile Cache<K, V> defaultCache;

    private VarExpiration<K, V> expiration;

    public CaffeineCache() {
    }

    @Override
    public void set(@NonNull K key, @NonNull V value, long expired, @NonNull TimeUnit unit) {
        this.initializeCache();
        this.expiration.put(key, value, expired, unit);
    }

    @Override
    public V get(K key) {
        return this.getCache().getIfPresent(key);
    }

    @Override
    public Boolean exists(K key) {
        return this.get(key) != null;
    }

    @Override
    public synchronized Boolean setIfAbsent(@NonNull K key, @NonNull V value, long expired, @NonNull TimeUnit unit) {
        V oldValue = this.get(key);
        if (oldValue != null) {
            return Boolean.FALSE;
        }
        this.expiration.put(key, value, expired, unit);
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(K key) {
        this.getCache().invalidate(key);
        return Boolean.TRUE;
    }

    private Cache<K, V> newCacheInstance(long expired, TimeUnit unit) {
        return Caffeine.newBuilder().initialCapacity(1)
            .expireAfter(new Expiry<Object, Object>() {
                @Override
                public long expireAfterCreate(@NonNull Object key,
                    @NonNull Object value, long currentTime) {
                    return unit.toNanos(expired);
                }

                @Override
                public long expireAfterUpdate(@NonNull Object key,
                    @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                    return currentDuration;
                }

                @Override
                public long expireAfterRead(@NonNull Object key,
                    @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                    return currentDuration;
                }
            })
            .build();
    }

    private void initializeCache() {
        if (this.defaultCache == null) {
            synchronized (this) {
                if (this.defaultCache == null) {
                    this.defaultCache = this.newCacheInstance(this.timeout, this.timeUnit);
                }
                this.expiration = this.defaultCache.policy()
                    .expireVariably()
                    .orElseThrow(() -> new IllegalArgumentException("请设置缓存超时时间"));
            }
        }
    }

    private Cache<K, V> getCache() {
        this.initializeCache();
        return this.defaultCache;
    }

}
