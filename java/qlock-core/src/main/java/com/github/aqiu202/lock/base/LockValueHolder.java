package com.github.aqiu202.lock.base;

/**
 * <pre>LockValueHolder</pre>
 *
 * @author aqiu 2020/12/2 15:14
 **/
public abstract class LockValueHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setValue(String value) {
        threadLocal.set(value);
    }

    public static String getValue() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
