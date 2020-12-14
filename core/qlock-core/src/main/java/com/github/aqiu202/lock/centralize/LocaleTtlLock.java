package com.github.aqiu202.lock.centralize;


import com.github.aqiu202.lock.base.CacheLock;
import com.github.aqiu202.ttl.data.StringTtlCache;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <pre>集中式缓存实现的锁</pre>
 *
 * @author aqiu
 **/
public class LocaleTtlLock implements CacheLock, InitializingBean {

    protected StringTtlCache cache;

    public LocaleTtlLock() {
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
