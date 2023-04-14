package com.github.aqiu202.lock.base;


import com.github.aqiu202.lock.centralize.LocaleTtlLock;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;

import java.util.Collection;

/**
 * <pre>{@link DefaultLockCodeExecutor}</pre>
 *
 * @author aqiu 2020/12/28 16:49
 **/
public class DefaultLockCodeExecutor implements LockCodeExecutor {

    private Lock lock;

    public DefaultLockCodeExecutor() {
        this(new LocaleTtlLock(new StringCaffeineCache()));
    }

    public DefaultLockCodeExecutor(Lock lock) {
        this.lock = lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public SimpleLockCodeRunner key(String key) {
        return new SimpleLockCodeRunner(this.lock, key);
    }

    @Override
    public MultipleLockCodeRunner keys(Collection<String> keys) {
        return new MultipleLockCodeRunner(this.lock, keys);
    }
}
