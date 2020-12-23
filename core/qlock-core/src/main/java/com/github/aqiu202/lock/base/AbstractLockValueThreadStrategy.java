package com.github.aqiu202.lock.base;

/**
 * <pre>{@link AbstractLockValueThreadStrategy}</pre>
 *
 * @author aqiu 2020/12/23 16:21
 **/
public abstract class AbstractLockValueThreadStrategy implements LockValueStrategy {

    public abstract ThreadLocal<String> getThreadLocal();

    @Override
    public void setValue(String value) {
        this.getThreadLocal().set(value);
    }

    @Override
    public String getValue() {
        return this.getThreadLocal().get();
    }

    @Override
    public void remove() {
        this.getThreadLocal().remove();
    }
}
