package com.github.aqiu202.lock.redisson;

import com.github.aqiu202.lock.base.KeyLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRedissonKeyLock implements KeyLock {

    protected RedissonClient redissonClient;

    protected AbstractRedissonKeyLock() {
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Boolean release(String key) {
        try {
            this.getLock(key).unlock();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean acquire(String key, long expires, TimeUnit timeUnit) {
        try {
            return this.getLock(key).tryLock(expires, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public Boolean acquire(String key) {
        return this.getLock(key).tryLock();
    }

    protected abstract RLock getLock(String key);

}
