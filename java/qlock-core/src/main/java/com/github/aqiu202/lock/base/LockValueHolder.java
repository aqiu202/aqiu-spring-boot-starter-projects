package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>LockValueHolder</pre>
 *
 * @author aqiu 2020/12/2 15:14
 **/
public abstract class LockValueHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String setIfAbsent(String value) {
        String old = getValue();
        if(old == null) {
            threadLocal.set(value);
            return value;
        }
        return old;
    }

    public static void setValue(String value) {
        threadLocal.set(value);
    }

    public static String getValue() {
        return threadLocal.get();
    }

    public static boolean hasValue() {
        return Objects.nonNull(getValue());
    }

    public static void remove() {
        threadLocal.remove();
    }
}
