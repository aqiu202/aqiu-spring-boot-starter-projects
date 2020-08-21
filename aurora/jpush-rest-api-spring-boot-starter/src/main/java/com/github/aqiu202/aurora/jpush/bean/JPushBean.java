package com.github.aqiu202.aurora.jpush.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jpush.config")
public final class JPushBean {

    public JPushBean() {
    }

    private String appKey = "";

    private String masterSecret = "";

    private boolean prod = false;

    private int timeToLive = 86400;

    private String pushUrl = "https://api.jpush.cn/v3/push";

    private String cidUrl = "https://api.jpush.cn/v3/push/cid";

    private int timeout = 5000;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getCidUrl() {
        return cidUrl;
    }

    public void setCidUrl(String cidUrl) {
        this.cidUrl = cidUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isProd() {
        return prod;
    }

    public void setProd(boolean prod) {
        this.prod = prod;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

}
