package com.github.aqiu202.wechat.wxpay;

import com.github.aqiu202.wechat.wxpay.bean.MyWxPayConfig;
import com.github.aqiu202.wechat.wxpay.result.UnifiedOrderResult;
import com.github.aqiu202.wechat.wxpay.sdk.WXPay;
import java.util.Map;

public class WXPayHelper {

    private final MyWxPayConfig wxPayConfig;

    public WXPayHelper(MyWxPayConfig wxPayConfig) {
        this.wxPayConfig = wxPayConfig;
    }

    public UnifiedOrderResult unifiedOrder(Map<String, String> reqData) {
        return build().unifiedOrder(reqData);
    }

    public UnifiedOrderResult unifiedOrder(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().unifiedOrder(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public UnifiedOrderResult reUnifiedOrder(String packageStr) {
        return build().rebuild(packageStr);
    }

    public Map<String, String> microPay(Map<String, String> reqData) {
        return build().microPay(reqData);
    }

    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().microPay(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> microPayWithPos(Map<String, String> reqData) {
        return build().microPayWithPos(reqData);
    }

    public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) {
        return build().microPayWithPos(reqData, connectTimeoutMs);
    }

    public Map<String, String> orderQuery(Map<String, String> reqData) {
        return build().orderQuery(reqData);
    }

    public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().orderQuery(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> reverse(Map<String, String> reqData) {
        return build().reverse(reqData);
    }

    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().reverse(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> closeOrder(Map<String, String> reqData) {
        return build().closeOrder(reqData);
    }

    public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().closeOrder(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> refund(Map<String, String> reqData) {
        return build().refund(reqData);
    }

    public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().refund(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> refundQuery(Map<String, String> reqData) {
        return build().refundQuery(reqData);
    }

    public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().refundQuery(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> downloadBill(Map<String, String> reqData) {
        return build().downloadBill(reqData);
    }

    public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().downloadBill(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> report(Map<String, String> reqData) {
        return build().report(reqData);
    }

    public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().report(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> shortUrl(Map<String, String> reqData) {
        return build().shortUrl(reqData);
    }

    public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().shortUrl(reqData, connectTimeoutMs, readTimeoutMs);
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) {
        return build().authCodeToOpenid(reqData);
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        return build().authCodeToOpenid(reqData, connectTimeoutMs, readTimeoutMs);
    }

    private WXPay build() {
        return new WXPay(wxPayConfig);
    }
}
