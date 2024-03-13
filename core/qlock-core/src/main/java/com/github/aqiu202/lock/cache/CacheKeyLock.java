package com.github.aqiu202.lock.cache;

import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.ttl.data.StringTtlCache;
import java.util.concurrent.TimeUnit;

/**
 * <pre>CacheKeyLock</pre>
 *
 * @author aqiu 2020/12/1 13:24
 **/
public interface CacheKeyLock extends KeyLock {

    String STRING_VALUE = "1";

    StringTtlCache getCache();

    default void setTimeout(long timeout) {
        this.getCache().setTimeout(timeout);
    }

    default void setTimeUnit(TimeUnit timeUnit) {
        this.getCache().setTimeUnit(timeUnit);
    }
}
