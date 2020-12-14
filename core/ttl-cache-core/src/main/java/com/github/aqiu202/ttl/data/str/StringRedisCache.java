package com.github.aqiu202.ttl.data.str;

import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.impl.RedisCache;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public class StringRedisCache extends RedisCache<String, String> implements
        StringTtlCache {

    public StringRedisCache(
            RedisConnectionFactory connectionFactory) {
        super(new StringRedisTemplate(connectionFactory));
    }
}
