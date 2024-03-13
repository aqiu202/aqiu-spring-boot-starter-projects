package com.github.aqiu202.lock.base;


import com.github.aqiu202.lock.cache.SimpleCacheKeyLock;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;

import java.util.Collection;

/**
 * <pre>{@link DefaultLockCodeExecutor}</pre>
 *
 * @author aqiu 2020/12/28 16:49
 **/
public class DefaultLockCodeExecutor implements LockCodeExecutor {

    private KeyLock keyLock;

    public DefaultLockCodeExecutor() {
        this(new SimpleCacheKeyLock(new StringCaffeineCache()));
    }

    public DefaultLockCodeExecutor(KeyLock keyLock) {
        this.keyLock = keyLock;
    }

    public void setLock(KeyLock keyLock) {
        this.keyLock = keyLock;
    }

    @Override
    public SimpleLockCodeRunner key(String key) {
        return new SimpleLockCodeRunner(this.keyLock, key);
    }

    @Override
    public MultipleLockCodeRunner keys(Collection<String> keys) {
        return new MultipleLockCodeRunner(this.keyLock, keys);
    }
}
