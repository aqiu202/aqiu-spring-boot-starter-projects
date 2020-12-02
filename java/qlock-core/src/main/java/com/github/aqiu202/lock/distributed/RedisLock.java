package com.github.aqiu202.lock.distributed;

import com.github.aqiu202.ttl.data.str.StringRedisCache;
import com.github.aqiu202.lock.centralize.TtlLocaleLock;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <pre>集中式缓存实现的锁</pre>
 * @author aqiu
 **/
public class RedisLock extends TtlLocaleLock {

    public RedisLock(StringRedisTemplate stringRedisTemplate) {
        super(new StringRedisCache(stringRedisTemplate));
    }

}
