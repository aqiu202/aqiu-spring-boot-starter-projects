package com.github.aqiu202.lock.distributed;

import com.github.aqiu202.lock.centralize.LocaleTtlLock;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <pre>集中式缓存实现的锁</pre>
 *
 * @author aqiu
 **/
public class RedisTtlLock extends LocaleTtlLock {

//    public RedisTtlLock(StringRedisTemplate stringRedisTemplate) {
//        this.cache = new StringRedisCache(stringRedisTemplate);
//    }

}
