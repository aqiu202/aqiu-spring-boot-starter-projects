package com.github.aqiu202.lock.redisson;

import org.redisson.api.RLock;

/**
 * 基于redisson的可重入锁
 * @author aqiu
 */
public class ReentrantRedissonKeyLock extends AbstractRedissonKeyLock {
    @Override
    protected RLock getLock(String key) {
        return this.redissonClient.getFencedLock(key);
    }
}
