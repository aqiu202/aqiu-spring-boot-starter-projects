package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>{@link LockValueStrategy}</pre>
 *
 * @author aqiu 2020/12/21 13:08
 **/
public interface LockValueStrategy {

    void setValue(String value);

    String getValue();

    void remove();

    default String setIfAbsent(String value) {
        String old = this.getValue();
        if (old == null) {
            this.setValue(value);
            return value;
        }
        return old;
    }

    default boolean hasValue() {
        return Objects.nonNull(this.getValue());
    }

}
