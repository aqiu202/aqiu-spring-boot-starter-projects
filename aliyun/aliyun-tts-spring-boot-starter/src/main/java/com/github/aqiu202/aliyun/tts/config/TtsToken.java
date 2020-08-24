package com.github.aqiu202.aliyun.tts.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnMissingBean
@ConfigurationProperties(prefix = "aliyun.tts")
public class TtsToken {

    /**
     * 阿里云智能语音交互项目appKey
     */
    private String appKey;
    /**
     * 阿里云accessKeyId
     */
    private String accessKeyId;
    /**
     * 阿里云accessKeySecret
     */
    private String accessKeySecret;

    private String url;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getUrl() {
        return url;
    }

    public TtsToken setUrl(String url) {
        this.url = url;
        return this;
    }
}
