package com.github.aqiu202.lock.base;

/**
 * <pre>{@link ResourceLockedException}</pre>
 *
 * @author aqiu 2020/12/31 10:19
 **/
public class ResourceLockedException extends RuntimeException {

    public ResourceLockedException() {
        super();
    }

    public ResourceLockedException(String message) {
        super(message);
    }

    public ResourceLockedException(Throwable throwable) {
        super(throwable);
    }

    public ResourceLockedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
