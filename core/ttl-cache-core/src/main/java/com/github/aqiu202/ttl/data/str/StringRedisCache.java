package com.github.aqiu202.ttl.data.str;

import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.impl.RedisCache;

public class StringRedisCache extends RedisCache<String, String> implements
        StringTtlCache {

}
