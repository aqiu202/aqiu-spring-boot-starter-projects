package com.github.aqiu202.lock.base;

import java.util.concurrent.TimeUnit;

/**
 * <pre>锁</pre>
 *
 * @author aqiu 2020/11/24 20:52
 **/
public interface Lock {

    Boolean acquire(String key);

    Boolean acquire(String key, long expires, TimeUnit timeUnit);

    Boolean release(String key);

    Boolean release(String key, long expired, TimeUnit timeUnit);
}
