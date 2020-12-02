package com.github.aqiu202.lock.centralize;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.lock.base.AbstractReentrantLock;
import com.github.aqiu202.ttl.data.StringTtlCache;

/**
 * <pre>ReentrantTtlLocaleLock</pre>
 *
 * @author aqiu 2020/12/2 15:39
 **/
public class ReentrantTtlLocaleLock extends AbstractReentrantLock {

    public ReentrantTtlLocaleLock(StringTtlCache cacheable, IdGenerator<?> idGenerator) {
        super(cacheable, idGenerator);
    }

    public ReentrantTtlLocaleLock(StringTtlCache cacheable) {
        super(cacheable);
    }

}
