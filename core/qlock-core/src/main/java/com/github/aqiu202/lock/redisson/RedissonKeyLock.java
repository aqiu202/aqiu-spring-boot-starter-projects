package com.github.aqiu202.lock.redisson;

import org.redisson.api.RLock;

/**
 * 基于Redisson的互斥锁
 * @author aqiu
 */
public class RedissonKeyLock extends AbstractRedissonKeyLock {
    @Override
    protected RLock getLock(String key) {
        return this.redissonClient.getLock(key);
    }
}
