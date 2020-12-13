package com.github.aqiu202.zk.prop;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>CuratorProperties</pre>
 *
 * @author aqiu 2020/12/10 16:01
 **/
@ConfigurationProperties(prefix = "curator")
public class CuratorProperties {

    private String connectString = "127.0.0.1:2181";

    private int sessionTimeout = Integer.getInteger("curator-default-session-timeout", 60 * 1000);

    private int connectionTimeout = Integer.getInteger("curator-default-connection-timeout", 15 * 1000);

    private int baseSleepTime = 1000;

    private int maxRetries = 3;

    private int maxSleepTime = Integer.MAX_VALUE;

    private List<AuthInfoProperties> auth = new ArrayList<>();

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getBaseSleepTime() {
        return baseSleepTime;
    }

    public void setBaseSleepTime(int baseSleepTime) {
        this.baseSleepTime = baseSleepTime;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxSleepTime() {
        return maxSleepTime;
    }

    public void setMaxSleepTime(int maxSleepTime) {
        this.maxSleepTime = maxSleepTime;
    }

    public List<AuthInfoProperties> getAuth() {
        return auth;
    }

    public void setAuths(List<AuthInfoProperties> auth) {
        this.auth = auth;
    }
}
