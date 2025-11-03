package com.github.aqiu202.ttl.data.impl;

import com.github.aqiu202.ttl.data.TtlCache;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

public abstract class AbstractTtlCache<K, V> implements TtlCache<K, V> {

    protected long timeout = DEFAULT_EXPIRED;

    protected TimeUnit timeUnit = DEFAULT_TIME_UNIT;

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public void set(@Nonnull K key, @Nonnull V value) {
        this.set(key, value, this.timeout, this.timeUnit);
    }

    @Override
    public Boolean setIfAbsent(@Nonnull K key, @Nonnull V value) {
        return this.setIfAbsent(key, value, this.timeout, this.timeUnit);
    }

}
