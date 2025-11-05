package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>LockValueHolder</pre>
 *
 * @author aqiu 2020/12/2 15:14
 **/
public abstract class LockValueHolder {

    private static volatile ValueHoldStrategy<String> strategy;

    static LockValueStrategyMode mode = LockValueStrategyMode.thread;

    public static String setIfAbsent(String value) {
        return getStrategy().setIfAbsent(value);
    }

    public static void setValue(String value) {
        getStrategy().setValue(value);
    }

    public static String getValue() {
        return getStrategy().getValue();
    }

    public static boolean hasValue() {
        return getStrategy().hasValue();
    }

    public static void remove() {
        getStrategy().remove();
    }

    private static ValueHoldStrategy<String> getStrategy() {
        if (strategy == null) {
            synchronized (LockValueHolder.class) {
                if (strategy == null) {
                    strategy = switchStrategy(mode);
                }
            }
        }
        return strategy;
    }

    private static ValueHoldStrategy<String> switchStrategy(LockValueStrategyMode mode) {
        switch (mode) {
            case inheritable_thread:
                return new LockValueInheritableThreadStrategy();
            case thread:
            default:
                return new LockValueThreadStrategy();
        }
    }

}
