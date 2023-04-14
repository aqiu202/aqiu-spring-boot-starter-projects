package com.github.aqiu202.lock.distributed;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

/**
 * <pre>ZookeeperLock</pre>
 *
 * @author aqiu 2020/11/24 16:49
 **/
public class ZookeeperLock extends AbstractZookeeperLock {


    @Override
    protected InterProcessLock createLock(String key) {
        return new InterProcessSemaphoreMutex(this.getCuratorFramework(), key);
    }

}
