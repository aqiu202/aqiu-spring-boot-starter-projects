package com.github.aqiu202.lock.zk;

import com.github.aqiu202.lock.base.KeyLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractZookeeperKeyLock implements KeyLock {

    private final Map<String, InterProcessLock> interProcessLockResource = new ConcurrentHashMap<>();

    protected CuratorFramework curatorFramework;

    protected AbstractZookeeperKeyLock() {
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    public void setCuratorFramework(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public Boolean release(String key) {
        try {
            this.currentLock(key).release();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean acquire(String key, long expires, TimeUnit timeUnit) {
        try {
            this.currentLock(key).acquire(expires, timeUnit);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean acquire(String key) {
        try {
            this.currentLock(key).acquire();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public InterProcessLock currentLock(String key) {
        InterProcessLock lock;
        if ((lock = this.interProcessLockResource.get(key)) == null) {
            lock = this.createLock(key);
            this.interProcessLockResource.put(key, lock);
        }
        return lock;
    }

    protected abstract InterProcessLock createLock(String key);

}
