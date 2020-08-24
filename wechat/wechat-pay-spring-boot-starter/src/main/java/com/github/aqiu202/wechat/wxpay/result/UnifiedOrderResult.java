package com.github.aqiu202.wechat.wxpay.result;

import java.util.HashMap;
import java.util.Map;

public class UnifiedOrderResult {

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 商户ID
     */
    private String mchId;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 签名类型
     */
    private String signType;
    /**
     * package小程序需要该字段
     */
    private String packageStr;
    /**
     * 参数签名（校验用）
     */
    private String paySign;
    /**
     * 预下单ID
     */
    private String prepayId;

    private final int status;
    private final Map<String, String> error;

    private UnifiedOrderResult() {
        this.status = 0;
        this.error = null;
    }

    private UnifiedOrderResult(Map<String, String> error) {
        this.status = 500;
        this.error = error;
    }

    public static UnifiedOrderResult ok() {
        return new UnifiedOrderResult();
    }

    public static UnifiedOrderResult error(Map<String, String> error) {
        return new UnifiedOrderResult(error);
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * @return the nonceStr
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * @param nonceStr the nonceStr to set
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    /**
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the signType
     */
    public String getSignType() {
        return signType;
    }

    /**
     * @param signType the signType to set
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }

    /**
     * @return the packageStr
     */
    public String getPackage() {
        return packageStr;
    }

    /**
     * @param packageStr the packageStr to set
     */
    public void setPackage(String packageStr) {
        this.packageStr = packageStr;
    }

    /**
     * @return the paySign
     */
    public String getPaySign() {
        return paySign;
    }

    /**
     * @param paySign the paySign to set
     */
    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the error
     */
    public Map<String, String> getError() {
        return error;
    }

    public Map<String, String> map() {
        Map<String, String> res = new HashMap<>();
        res.put("appId", appId);
        res.put("nonceStr", nonceStr);
        res.put("timeStamp", timeStamp);
        res.put("signType", signType);
        res.put("package", packageStr);
        return res;
    }

}
