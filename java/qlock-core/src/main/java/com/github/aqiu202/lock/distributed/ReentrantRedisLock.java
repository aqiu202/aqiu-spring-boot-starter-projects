package com.github.aqiu202.lock.distributed;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.lock.base.AbstractReentrantLock;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <pre>集中式缓存实现的锁</pre>
 *
 * @author aqiu
 **/
public class ReentrantRedisLock extends AbstractReentrantLock {

    public ReentrantRedisLock(StringRedisTemplate stringRedisTemplate) {
        super(new StringRedisCache(stringRedisTemplate));
    }

    public ReentrantRedisLock(StringRedisTemplate stringRedisTemplate, IdGenerator<?> idGenerator) {
        super(new StringRedisCache(stringRedisTemplate), idGenerator);
    }

}
