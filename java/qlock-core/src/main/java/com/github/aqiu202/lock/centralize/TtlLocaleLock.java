package com.github.aqiu202.lock.centralize;

import com.github.aqiu202.lock.base.CacheLock;
import com.github.aqiu202.lock.base.LockValueHolder;
import com.github.aqiu202.ttl.data.StringTtlCache;
import java.util.concurrent.TimeUnit;

/**
 * <pre>集中式缓存实现的锁</pre>
 *
 * @author aqiu
 **/
public class TtlLocaleLock implements CacheLock {

    protected final StringTtlCache cacheable;

    public TtlLocaleLock(StringTtlCache cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public StringTtlCache getCache() {
        return this.cacheable;
    }

    @Override
    public Boolean release(String key, long expired, TimeUnit timeUnit) {
        return this.cacheable.delete(key, expired, timeUnit);
    }

    @Override
    public Boolean release(String key) {
        return this.cacheable.delete(key);
    }

    @Override
    public Boolean acquire(String key, long expired, TimeUnit timeUnit) {
        return this.cacheable.setIfAbsent(key, STRING_VALUE, expired, timeUnit);
    }

    @Override
    public Boolean acquire(String key) {
        return this.cacheable.setIfAbsent(key, STRING_VALUE);
    }

}
