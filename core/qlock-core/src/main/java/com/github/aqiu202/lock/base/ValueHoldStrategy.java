package com.github.aqiu202.lock.base;

import java.util.Objects;

/**
 * <pre>{@link ValueHoldStrategy}</pre>
 *
 * @author aqiu 2020/12/21 13:08
 **/
public interface ValueHoldStrategy<T> {

    void setValue(T value);

    T getValue();

    void remove();

    default T setIfAbsent(T value) {
        T old = this.getValue();
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
