package com.github.aqiu202.lock.centralize;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.lock.base.AbstractReentrantTtlLock;
import com.github.aqiu202.ttl.data.StringTtlCache;

/**
 * <pre>ReentrantTtlLocaleLock</pre>
 *
 * @author aqiu 2020/12/2 15:39
 **/
public class ReentrantLocaleTtlLock extends AbstractReentrantTtlLock {

    public ReentrantLocaleTtlLock(IdGenerator<?> idGenerator) {
        super(idGenerator);
    }

//    public ReentrantLocaleTtlLock(StringTtlCache cacheable) {
//        super(cacheable);
//    }

}
