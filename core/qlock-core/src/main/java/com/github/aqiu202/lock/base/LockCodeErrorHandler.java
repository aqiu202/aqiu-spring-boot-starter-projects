package com.github.aqiu202.lock.base;

import java.util.function.Consumer;

public class LockCodeErrorHandler implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) {
        throw new RuntimeException(throwable);
    }
}
