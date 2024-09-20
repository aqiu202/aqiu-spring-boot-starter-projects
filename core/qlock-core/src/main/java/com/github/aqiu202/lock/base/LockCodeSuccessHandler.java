package com.github.aqiu202.lock.base;

import java.util.function.Consumer;

public class LockCodeSuccessHandler<T> implements Consumer<T> {
    @Override
    public void accept(T result) {
    }
}
