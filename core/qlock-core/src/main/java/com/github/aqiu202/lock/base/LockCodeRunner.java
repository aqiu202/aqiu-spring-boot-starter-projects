package com.github.aqiu202.lock.base;

import java.util.concurrent.Callable;

/**
 * <pre>{@link LockCodeRunner}</pre>
 *
 * @author aqiu 2020/12/28 16:30
 **/
public interface LockCodeRunner {

    <T> T run(String key, LockCodeRunnerParam<T> param);

    void run(String key, Runnable runnable);

    <T> T run(String key, Callable<T> callable);
}
