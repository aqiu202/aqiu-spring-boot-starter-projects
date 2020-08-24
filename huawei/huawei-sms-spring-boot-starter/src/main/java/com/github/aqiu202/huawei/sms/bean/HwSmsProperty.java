package com.github.aqiu202.huawei.sms.bean;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

@ConfigurationProperties("huawei.sms")
public final class HwSmsProperty {

    private String url = "https://rtcsms.cn-north-1.myhuaweicloud.com:10743/sms/batchSendSms/v1"; //APP接入地址+接口访问URI
    private String appKey;
    private String appSecret;
    private String sender = "8819111319507"; //国内短信签名通道号或国际/港澳台短信通道号
    private Map<String, String> templates = new HashMap<>(); //模板ID

    //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
    //国际/港澳台短信不用关注该参数
    private String signature = "红柿子软件"; //签名名称

    //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
    private String statusCallBack;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        Assert.hasText(appKey, "appKey not be empty");
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        Assert.hasText(appSecret, "appSecret not be empty");
        this.appSecret = appSecret;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatusCallBack() {
        return statusCallBack;
    }

    public void setStatusCallBack(String statusCallBack) {
        this.statusCallBack = statusCallBack;
    }

}
