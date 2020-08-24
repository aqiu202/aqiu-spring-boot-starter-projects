package com.github.aqiu202.cache.data.str;

import com.github.aqiu202.cache.data.StringTimeLimitedCache;
import com.github.aqiu202.cache.data.impl.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;

public class StringRedisCache extends RedisCache<String, String> implements
        StringTimeLimitedCache {

    public StringRedisCache(
            StringRedisTemplate cache) {
        super(cache);
    }
}
