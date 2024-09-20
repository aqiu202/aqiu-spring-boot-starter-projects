package com.github.aqiu202.lock.base;

import java.util.Collection;

/**
 * <pre>{@link LockCodeExecutor}</pre>
 *
 * @author aqiu 2020/12/28 16:30
 **/
public interface LockCodeExecutor {

    SimpleLockCodeRunner key(String key);

    MultipleLockCodeRunner keys(Collection<String> keys);

}
