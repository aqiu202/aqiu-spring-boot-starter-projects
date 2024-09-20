package com.github.aqiu202.lock.base;

import java.util.Collection;

public class MultipleLockCodeRunner extends AbstractLockCodeRunner<Collection<String>> {

    private final Collection<String> keys;

    public MultipleLockCodeRunner(KeyLock keyLock, Collection<String> keys) {
        super(keyLock);
        this.keys = keys;
    }

    @Override
    public Collection<String> getKey() {
        return this.keys;
    }

    @Override
    protected void getLock(Collection<String> keys) {
        KeyLock keyLock = this.getLock();
        for (String key : keys) {
            Boolean getLock = keyLock.acquire(key);
            if (getLock == null || !getLock) {
                throw new ResourceLockedException("资源已被锁定，无法访问");
            }
        }
    }

    @Override
    protected void releaseKey(Collection<String> keys) {
        KeyLock keyLock = this.getLock();
        for (String key : keys) {
            keyLock.release(key);
        }
    }
}
