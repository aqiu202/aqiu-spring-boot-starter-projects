package com.github.aqiu202.ttl.data;

import java.util.concurrent.TimeUnit;
import org.springframework.lang.Nullable;

import jakarta.annotation.Nonnull;

public interface TtlCache<K, V> {

    long DEFAULT_EXPIRED = 3600;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    void set(@Nonnull K key, @Nonnull V value);

    void set(@Nonnull K key, @Nonnull V value, long expired, @Nonnull TimeUnit unit);

    V get(@Nonnull K key);

    Boolean exists(@Nonnull K key);

    @Nullable
    Boolean setIfAbsent(@Nonnull K key, @Nonnull V value, long expired, @Nonnull TimeUnit unit);

    Boolean setIfAbsent(@Nonnull K key, @Nonnull V value);

    Boolean delete(@Nonnull K key);

    void setTimeout(long timeout);

    void setTimeUnit(TimeUnit timeUnit);

}
