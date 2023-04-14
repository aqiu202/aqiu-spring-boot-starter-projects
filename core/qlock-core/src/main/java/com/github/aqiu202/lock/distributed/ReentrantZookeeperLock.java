package com.github.aqiu202.lock.distributed;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * <pre>ReentrantZookeeperLock</pre>
 *
 * @author aqiu 2023/4/13 13:49
 **/
public class ReentrantZookeeperLock extends AbstractZookeeperLock {


    @Override
    protected InterProcessLock createLock(String key) {
        return new InterProcessMutex(this.getCuratorFramework(), key);
    }

}
