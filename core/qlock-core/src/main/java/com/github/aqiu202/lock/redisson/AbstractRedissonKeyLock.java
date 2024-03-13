package com.github.aqiu202.lock.redisson;

import com.github.aqiu202.lock.base.KeyLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedissonKeyLock implements KeyLock {

    private final Map<String, RLock> lockResource = new ConcurrentHashMap<>();

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
    public Boolean release(String key, long expired, TimeUnit timeUnit) {
        return this.release(key);
    }

    @Override
    public Boolean release(String key) {
        try {
            this.currentLock(key).unlock();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean acquire(String key, long expires, TimeUnit timeUnit) {
        try {
            return this.currentLock(key).tryLock(expires, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public Boolean acquire(String key) {
        return this.currentLock(key).tryLock();
    }

    public RLock currentLock(String key) {
        RLock lock;
        if ((lock = this.lockResource.get(key)) == null) {
            lock = this.createLock(key);
            this.lockResource.put(key, lock);
        }
        return lock;
    }

    protected abstract RLock createLock(String key);

}
