package com.github.aqiu202.lock;

import com.github.aqiu202.id.generator.SnowFlakeIdGenerator;
import com.github.aqiu202.lock.base.ReentrantCacheKeyLock;
import com.github.aqiu202.lock.cache.CacheKeyLock;
import com.github.aqiu202.lock.cache.SimpleCacheKeyLock;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;
import org.junit.jupiter.api.Test;

public class LockTests {

    @Test
    public void test() {
        ReentrantCacheKeyLock lock = new ReentrantCacheKeyLock();
        lock.setCache(new StringCaffeineCache());
        lock.setIdGenerator(new SnowFlakeIdGenerator());
        CacheKeyLock cacheKeyLock = lock;
//        CacheKeyLock cacheKeyLock = new SimpleCacheKeyLock(new StringCaffeineCache());
        String key = "test";
        Boolean acquire = cacheKeyLock.acquire(key);
        System.out.println(acquire);
        Boolean r = cacheKeyLock.acquire(key);
        System.out.println(r);
        System.out.println(cacheKeyLock.release(key));
        System.out.println(cacheKeyLock.release(key));
    }
}
