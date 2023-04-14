package com.github.aqiu202.lock.base;

import java.util.Collection;

public class MultipleLockCodeRunner extends AbstractLockCodeRunner<Collection<String>> {

    private final Collection<String> keys;

    public MultipleLockCodeRunner(Lock lock, Collection<String> keys) {
        super(lock);
        this.keys = keys;
    }

    @Override
    public Collection<String> getKey() {
        return this.keys;
    }

    @Override
    protected void getLock(Collection<String> keys) {
        Lock lock = this.getLock();
        for (String key : keys) {
            Boolean getLock = lock.acquire(key);
            if (getLock == null || !getLock) {
                throw new ResourceLockedException("资源已被锁定，无法访问");
            }
        }
    }

    @Override
    protected void releaseKey(Collection<String> keys) {
        Lock lock = this.getLock();
        for (String key : keys) {
            lock.release(key);
        }
    }
}
