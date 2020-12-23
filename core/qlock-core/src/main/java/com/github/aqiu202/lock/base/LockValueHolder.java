package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>LockValueHolder</pre>
 *
 * @author aqiu 2020/12/2 15:14
 **/
public abstract class LockValueHolder {

    private static volatile LockValueStrategy strategy;

    static LockValueStrategyMode mode = LockValueStrategyMode.thread;


    public static String setIfAbsent(String value) {
        String old = getValue();
        if (old == null) {
            setValue(value);
            return value;
        }
        return old;
    }

    public static void setValue(String value) {
        getStrategy().setValue(value);
    }

    public static String getValue() {
        return getStrategy().getValue();
    }

    public static boolean hasValue() {
        return Objects.nonNull(getValue());
    }

    public static void remove() {
        getStrategy().remove();
    }

    private static LockValueStrategy getStrategy() {
        if (strategy == null) {
            synchronized (LockValueHolder.class) {
                if (strategy == null) {
                    strategy = switchStrategy(mode);
                }
            }
        }
        return strategy;
    }

    private static LockValueStrategy switchStrategy(LockValueStrategyMode mode) {
        if (mode == LockValueStrategyMode.inheritable_thread) {
            return new LockValueInheritableThreadStrategy();
        }
        return new LockValueThreadStrategy();
    }

}
