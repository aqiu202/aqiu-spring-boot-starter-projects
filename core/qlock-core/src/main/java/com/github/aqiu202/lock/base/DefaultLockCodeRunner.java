package com.github.aqiu202.lock.base;

import com.github.aqiu202.lock.centralize.LocaleTtlLock;
import com.github.aqiu202.ttl.data.str.StringCaffeineCache;
import java.util.concurrent.Callable;

/**
 * <pre>{@link DefaultLockCodeRunner}</pre>
 *
 * @author aqiu 2020/12/28 16:49
 **/
public class DefaultLockCodeRunner implements LockCodeRunner {

    private Lock lock;

    public DefaultLockCodeRunner() {
        final LocaleTtlLock localeTtlLock = new LocaleTtlLock();
        localeTtlLock.setCache(new StringCaffeineCache());
        this.lock = localeTtlLock;
    }

    public DefaultLockCodeRunner(Lock lock) {
        this.lock = lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public <T> T run(String key, LockCodeRunnerParam<T> param) {
        this.getLock(key);
        T result = null;
        try {
            result = param.getRunner().get();
            param.getSuccess().accept(result);
            return result;
        } catch (Throwable throwable) {
            param.getError().accept(throwable);
        } finally {
            this.lock.release(key);
        }
        return result;
    }

    @Override
    public void run(String key, Runnable runnable) {
        this.getLock(key);
        try {
            runnable.run();
        } finally {
            this.lock.release(key);
        }
    }

    @Override
    public <T> T run(String key, Callable<T> callable) {
        this.getLock(key);
        T result;
        try {
            result = callable.call();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            this.lock.release(key);
        }
        return result;
    }

    private void getLock(String key) {
        Boolean getLock = this.lock.acquire(key);
        if (!getLock) {
            throw new ResourceLockedException("资源已被锁定，无法访问");
        }
    }

}
