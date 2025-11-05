package com.github.aqiu202.lock.base;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.IdGeneratorFactory;
import com.github.aqiu202.lock.cache.SimpleCacheKeyLock;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * <pre>基于缓存的简单可重入锁的实现</pre>
 *
 * @author aqiu 2020/12/2 15:57
 **/
public class ReentrantCacheKeyLock extends SimpleCacheKeyLock {

    protected IdGenerator<?> idGenerator;

    protected IdGeneratorFactory idGeneratorFactory;

    public IdGenerator<?> getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator<?> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public IdGeneratorFactory getIdGeneratorFactory() {
        return idGeneratorFactory;
    }

    public void setIdGeneratorFactory(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
    }

    public void setLockValueStrategyMode(LockValueStrategyMode mode) {
        LockValueHolder.mode = mode;
    }

    @Override
    public Boolean release(String key) {
        final String value = this.cache.get(key);
        if (Objects.equals(value, LockOwnerHolder.consumeLockOwner())) {
            return this.doRelease(key);
        }
        return false;
    }

    @Override
    public Boolean acquire(String key, long expired, TimeUnit timeUnit) {
        String value = String.valueOf(this.idGenerator.nextId());
        LockOwnerHolder.addLockOwner(value);
        value = LockValueHolder.setIfAbsent(value);
        final Boolean result = this.doAcquire(key, value, expired, timeUnit);
        if (result != null && !result) {
            return Objects.equals(this.cache.get(key), value);
        }
        return result;
    }

    @Override
    public Boolean acquire(String key) {
        String value = String.valueOf(this.idGenerator.nextId());
        LockOwnerHolder.addLockOwner(value);
        value = LockValueHolder.setIfAbsent(value);
        final Boolean result = this.doAcquire(key, value);
        if (result != null && !result) {
            return Objects.equals(this.cache.get(key), value);
        }
        return result;
    }

    @Nullable
    public Boolean doAcquire(String key, String value) {
        return this.cache.setIfAbsent(key, value);
    }

    @Nullable
    public Boolean doAcquire(String key, String value, long expired, TimeUnit timeUnit) {
        return this.cache.setIfAbsent(key, value, expired, timeUnit);
    }

    @Nullable
    public Boolean doRelease(String key) {
        LockValueHolder.remove();
        return super.release(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initIdGenerator();
        Assert.notNull(this.idGenerator, "ID Generator无法构建");
        super.afterPropertiesSet();
    }

    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.idGeneratorFactory.getIdGenerator();
        }
    }
}
