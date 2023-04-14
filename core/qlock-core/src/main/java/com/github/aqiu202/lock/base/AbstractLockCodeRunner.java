package com.github.aqiu202.lock.base;

import java.util.concurrent.Callable;

public abstract class AbstractLockCodeRunner<K> {

    private final Lock lock;

    protected AbstractLockCodeRunner(Lock lock) {
        this.lock = lock;
    }

    public <T> T tryRun(Callable<T> callable) {
        K key = this.getKey();
        this.getLock(key);
        T result;
        try {
            result = callable.call();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            this.releaseKey(key);
        }
        return result;
    }

    public void tryRun(Runnable runnable) {
        K key = this.getKey();
        this.getLock(key);
        try {
            runnable.run();
        } finally {
            this.releaseKey(key);
        }
    }

    public <T> T tryRun(LockCodeInvocation<T> invocation) {
        K key = this.getKey();
        this.getLock(key);
        T result = null;
        try {
            result = invocation.getRunner().get();
            invocation.getSuccessHandler().accept(result);
            return result;
        } catch (Throwable throwable) {
            invocation.getErrorHandler().accept(throwable);
        } finally {
            this.releaseKey(key);
        }
        return result;
    }

    public Lock getLock() {
        return lock;
    }

    protected abstract K getKey();

    protected abstract void getLock(K key);

    protected abstract void releaseKey(K key);

}
