package com.github.aqiu202.qlock.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "redisson")
public class RedissonProperty {

    /**
     * 单机redis地址
     */
    private String single;
    /**
     * 单机redis数据库配置（默认0）
     */
    private int singleDatabase = 0;
    /**
     * 集群redis节点地址
     */
    private List<String> nodes;

    /**
     * 看门狗的检查周期（防死锁）
     */
    private long lockWatchdogTimeout = 30 * 1000;

    /**
     * 是否启用Lua脚本缓存
     */
    private boolean useScriptCache = false;
    /**
     * 数据清理的最小延迟
     */
    private int minCleanUpDelay = 5;
    /**
     * 数据清理的最大延迟
     */
    private int maxCleanUpDelay = 30 * 60;
    /**
     * 每个删除操作删除的key数量
     */
    private int cleanUpKeysAmount = 100;

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public int getSingleDatabase() {
        return singleDatabase;
    }

    public void setSingleDatabase(int singleDatabase) {
        this.singleDatabase = singleDatabase;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public long getLockWatchdogTimeout() {
        return lockWatchdogTimeout;
    }

    public void setLockWatchdogTimeout(long lockWatchdogTimeout) {
        this.lockWatchdogTimeout = lockWatchdogTimeout;
    }

    public boolean isUseScriptCache() {
        return useScriptCache;
    }

    public void setUseScriptCache(boolean useScriptCache) {
        this.useScriptCache = useScriptCache;
    }

    public int getMinCleanUpDelay() {
        return minCleanUpDelay;
    }

    public void setMinCleanUpDelay(int minCleanUpDelay) {
        this.minCleanUpDelay = minCleanUpDelay;
    }

    public int getMaxCleanUpDelay() {
        return maxCleanUpDelay;
    }

    public void setMaxCleanUpDelay(int maxCleanUpDelay) {
        this.maxCleanUpDelay = maxCleanUpDelay;
    }

    public int getCleanUpKeysAmount() {
        return cleanUpKeysAmount;
    }

    public void setCleanUpKeysAmount(int cleanUpKeysAmount) {
        this.cleanUpKeysAmount = cleanUpKeysAmount;
    }
}
