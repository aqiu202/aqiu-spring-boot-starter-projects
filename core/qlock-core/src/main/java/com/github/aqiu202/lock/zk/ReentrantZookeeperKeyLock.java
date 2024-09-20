package com.github.aqiu202.lock.zk;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * <pre>Zookeeper可重入锁</pre>
 *
 * @author aqiu 2023/4/13 13:49
 **/
public class ReentrantZookeeperKeyLock extends AbstractZookeeperKeyLock {

    @Override
    protected InterProcessLock createLock(String key) {
        return new InterProcessMutex(this.getCuratorFramework(), key);
    }

}
