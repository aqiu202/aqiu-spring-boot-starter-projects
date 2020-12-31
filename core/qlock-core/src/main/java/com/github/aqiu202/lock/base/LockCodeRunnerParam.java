package com.github.aqiu202.lock.base;


import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

/**
 * <pre>{@link LockCodeRunnerParam}</pre>
 *
 * @author aqiu 2020/12/28 16:34
 **/
public class LockCodeRunnerParam<T> {

    private final Supplier<T> runner;
    private Consumer<T> success;
    private Consumer<? super Throwable> error;

    private LockCodeRunnerParam(@Nonnull Supplier<T> runner) {
        this.runner = runner;
    }

    public static <T> LockCodeRunnerParam<T> of(Supplier<T> runner) {
        return new LockCodeRunnerParam<>(runner);
    }

    public LockCodeRunnerParam<T> onSuccess(Consumer<T> success) {
        this.success = success;
        return this;
    }

    public LockCodeRunnerParam<T> onError(Consumer<? super Throwable> error) {
        this.error = error;
        return this;
    }

    public Supplier<T> getRunner() {
        return runner;
    }

    public Consumer<T> getSuccess() {
        return success;
    }

    public Consumer<? super Throwable> getError() {
        return error;
    }

}
