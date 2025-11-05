package com.github.aqiu202.lock.base;

/**
 * <pre>{@link AbstractValueHoldThreadStrategy}</pre>
 *
 * @author aqiu 2020/12/23 16:21
 **/
public abstract class AbstractValueHoldThreadStrategy<T> implements ValueHoldStrategy<T> {

    public abstract ThreadLocal<T> getThreadLocal();

    @Override
    public void setValue(T value) {
        this.getThreadLocal().set(value);
    }

    @Override
    public T getValue() {
        return this.getThreadLocal().get();
    }

    @Override
    public void remove() {
        this.getThreadLocal().remove();
    }
}
