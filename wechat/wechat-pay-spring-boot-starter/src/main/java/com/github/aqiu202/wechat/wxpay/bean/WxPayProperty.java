package com.github.aqiu202.wechat.wxpay.bean;


import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.SignType;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.TradeType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wx.pay.props")
public final class WxPayProperty {

    private String appID;
    private String mchID;
    private String key;
    private String cert;
    private SignType signType = SignType.MD5;
    private TradeType tradeType = TradeType.JSAPI;
    private String notifyUrl;
    private String refundNotifyUrl;

    /**
     * @return the appID
     */
    public String getAppID() {
        return appID;
    }

    /**
     * @param appID the appID to set
     */
    public void setAppID(String appID) {
        this.appID = appID;
    }

    /**
     * @return the mchID
     */
    public String getMchID() {
        return mchID;
    }

    /**
     * @param mchID the mchID to set
     */
    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the cert
     */
    public String getCert() {
        return cert;
    }

    /**
     * @param cert the cert to set
     */
    public void setCert(String cert) {
        this.cert = cert;
    }

    /**
     * @return the signType
     */
    public SignType getSignType() {
        return signType;
    }

    /**
     * @param signType the signType to set
     */
    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    /**
     * @return the tradeType
     */
    public TradeType getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType the tradeType to set
     */
    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * @return the notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }
}
