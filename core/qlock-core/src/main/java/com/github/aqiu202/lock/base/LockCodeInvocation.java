package com.github.aqiu202.lock.base;


import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <pre>{@link LockCodeInvocation}</pre>
 *
 * @author aqiu 2020/12/28 16:34
 **/
public class LockCodeInvocation<T> {

    private final Supplier<T> runner;
    private Consumer<T> successHandler = new LockCodeSuccessHandler<>();
    private Consumer<? super Throwable> errorHandler = new LockCodeErrorHandler();

    private LockCodeInvocation(@Nonnull Supplier<T> runner) {
        this.runner = runner;
    }

    public static <T> LockCodeInvocation<T> of(Supplier<T> runner) {
        return new LockCodeInvocation<>(runner);
    }

    public LockCodeInvocation<T> onSuccess(Consumer<T> success) {
        this.successHandler = success;
        return this;
    }

    public LockCodeInvocation<T> onError(Consumer<? super Throwable> error) {
        this.errorHandler = error;
        return this;
    }

    public Supplier<T> getRunner() {
        return runner;
    }

    public Consumer<T> getSuccessHandler() {
        return successHandler;
    }

    public Consumer<? super Throwable> getErrorHandler() {
        return errorHandler;
    }

}
