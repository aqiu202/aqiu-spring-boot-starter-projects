package com.github.aqiu202.lock.base;

/**
 * <pre>{@link LockValueInheritableThreadStrategy}</pre>
 *
 * @author aqiu 2020/12/21 13:08
 **/
public class LockValueInheritableThreadStrategy extends AbstractValueHoldThreadStrategy<String> {

    private final ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    @Override
    public ThreadLocal<String> getThreadLocal() {
        return this.threadLocal;
    }
}