package com.github.aqiu202.lock.base;

public class SimpleLockCodeRunner extends AbstractLockCodeRunner<String> {

    private final String key;

    public SimpleLockCodeRunner(KeyLock keyLock, String key) {
        super(keyLock);
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    protected void getLock(String key) {
        Boolean getLock = this.getLock().acquire(key);
        if (!getLock) {
            throw new ResourceLockedException("资源已被锁定，无法访问");
        }
    }

    @Override
    protected void releaseKey(String key) {
        this.getLock().release(key);
    }

}
