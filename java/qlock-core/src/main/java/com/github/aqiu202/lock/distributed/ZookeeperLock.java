package com.github.aqiu202.lock.distributed;

import com.github.aqiu202.lock.base.Lock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * <pre>ZookeeperLock</pre>
 *
 * @author aqiu 2020/11/24 16:49
 **/
public class ZookeeperLock implements Lock {

    private final Map<String, InterProcessLock> interProcessLockResource = new ConcurrentHashMap<>();

    private final CuratorFramework curatorFramework;

    public ZookeeperLock(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public Boolean release(String key, long expired, TimeUnit timeUnit) {
        return this.release(key);
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

    private InterProcessLock createLock(String key) {
        return new InterProcessMutex(this.curatorFramework, key);
    }

}
