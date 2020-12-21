package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>LockValueHolder</pre>
 *
 * @author aqiu 2020/12/2 15:14
 **/
public abstract class LockValueHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static final ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    static LockValueHolderStrategy strategy = LockValueHolderStrategy.thread;

    public static String setIfAbsent(String value) {
        String old = getValue();
        if (old == null) {
            setValue(value);
            return value;
        }
        return old;
    }

    public static void setValue(String value) {
        getHolder().set(value);
    }

    public static String getValue() {
        return getHolder().get();
    }

    public static boolean hasValue() {
        return Objects.nonNull(getValue());
    }

    public static void remove() {
        getHolder().remove();
    }

    private static ThreadLocal<String> getHolder() {
        if (strategy == LockValueHolderStrategy.inheritable_thread) {
            return inheritableThreadLocal;
        }
        return threadLocal;
    }

}
