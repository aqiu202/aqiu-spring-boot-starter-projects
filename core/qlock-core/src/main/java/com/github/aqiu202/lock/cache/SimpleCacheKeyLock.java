package com.github.aqiu202.lock.cache;


import com.github.aqiu202.ttl.data.StringTtlCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * <pre>基于缓存实现的锁</pre>
 *
 * @author aqiu
 **/
public class SimpleCacheKeyLock implements CacheKeyLock, InitializingBean {

    protected StringTtlCache cache;

    public SimpleCacheKeyLock() {
    }

    public SimpleCacheKeyLock(StringTtlCache cache) {
        this.cache = cache;
    }

    public void setCache(StringTtlCache cache) {
        this.cache = cache;
    }

    @Override
    public StringTtlCache getCache() {
        return this.cache;
    }

    @Override
    public Boolean release(String key, long expired, TimeUnit timeUnit) {
        return this.cache.delete(key, expired, timeUnit);
    }

    @Override
    public Boolean release(String key) {
        return this.cache.delete(key);
    }

    @Override
    public Boolean acquire(String key, long expired, TimeUnit timeUnit) {
        return this.cache.setIfAbsent(key, STRING_VALUE, expired, timeUnit);
    }

    @Override
    public Boolean acquire(String key) {
        return this.cache.setIfAbsent(key, STRING_VALUE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.cache, "cache 不能为空");
    }
}
