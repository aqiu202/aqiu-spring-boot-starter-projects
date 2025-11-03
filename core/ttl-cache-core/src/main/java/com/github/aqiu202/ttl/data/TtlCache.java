package com.github.aqiu202.ttl.data;

import java.util.concurrent.TimeUnit;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface TtlCache<K, V> {

    long DEFAULT_EXPIRED = 3600;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    void set(@NonNull K key, @NonNull V value);

    void set(@NonNull K key, @NonNull V value, long expired, @NonNull TimeUnit unit);

    V get(@NonNull K key);

    Boolean exists(@NonNull K key);

    @Nullable
    Boolean setIfAbsent(@NonNull K key, @NonNull V value, long expired, @NonNull TimeUnit unit);

    Boolean setIfAbsent(@NonNull K key, @NonNull V value);

    Boolean delete(@NonNull K key);

    void setTimeout(long timeout);

    void setTimeUnit(TimeUnit timeUnit);

}
