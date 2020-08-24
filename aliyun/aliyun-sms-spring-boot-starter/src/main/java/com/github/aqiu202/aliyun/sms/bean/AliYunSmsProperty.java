package com.github.aqiu202.aliyun.sms.bean;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

@ConfigurationProperties("aliyun.mns")
public final class AliYunSmsProperty {

    /**
     * 连接超时时间
     */
    private String connectTimeout = "10000";

    /**
     * 读取超时时间
     */
    private String readTimeout = "10000";

    /**
     * 短信API产品名称
     */
    private String product = "Dysmsapi";

    /**
     * 短信API产品域名
     */
    private String domain = "dysmsapi.aliyuncs.com";

    /**
     * accessKeyId
     */
    private String accessKeyId = "";

    /**
     * accessKeySecret
     */
    private String accessKeySecret = "";

    /**
     * regionId
     */
    private String regionId = "cn-hangzhou";

    /**
     * 签名名称
     */
    private String signName = "云通信";

    /**
     * 模板映射
     */
    private Map<String, String> templates;

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(String readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        Assert.hasText(accessKeyId, "accessKeyId not be empty");
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        Assert.hasText(accessKeySecret, "accessKeySecret not be empty");
        this.accessKeySecret = accessKeySecret;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }
}
