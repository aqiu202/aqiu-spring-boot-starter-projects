package com.github.aqiu202.lock.zk;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

/**
 * <pre>Zookeeper互斥锁</pre>
 *
 * @author aqiu 2020/11/24 16:49
 **/
public class ZookeeperKeyLock extends AbstractZookeeperKeyLock {

    @Override
    protected InterProcessLock createLock(String key) {
        return new InterProcessSemaphoreMutex(this.getCuratorFramework(), key);
    }

}
