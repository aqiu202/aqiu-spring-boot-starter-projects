package com.github.aqiu202.util.retry;

import java.util.concurrent.Callable;

public class RetryHelper {
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;

    public static void tryRun(Runnable runnable) {
        tryRun(runnable, DEFAULT_MAX_RETRY_TIMES);
    }

    public static void tryRun(Runnable runnable, int maxRetryTimes) {
        tryRun(runnable, maxRetryTimes, 0);
    }

    public static void tryRun(Runnable runnable, int maxRetryTimes, long delay) {
        new RetrievableTask(runnable).setMaxTryTimes(maxRetryTimes)
                .setDelay(delay)
                .run();
    }

    public static <T> T tryRun(Callable<T> callable) {
        return tryRun(callable, DEFAULT_MAX_RETRY_TIMES);
    }

    public static <T> T tryRun(Callable<T> callable, int maxRetryTimes) {
        return tryRun(callable, maxRetryTimes, 0);
    }

    public static <T> T tryRun(Callable<T> callable, int maxRetryTimes, long delay) {
        return new RetrievableCallable<T>(callable)
                .setMaxTryTimes(maxRetryTimes)
                .setDelay(delay)
                .call();

    }
}