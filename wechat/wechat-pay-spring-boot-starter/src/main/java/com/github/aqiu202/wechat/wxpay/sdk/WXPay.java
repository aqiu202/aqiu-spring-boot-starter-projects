package com.github.aqiu202.wechat.wxpay.sdk;


import com.github.aqiu202.wechat.wxpay.bean.MyWxPayConfig;
import com.github.aqiu202.wechat.wxpay.result.UnifiedOrderResult;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.SignType;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class WXPay {

    private static final Logger log = LoggerFactory.getLogger(WXPay.class);

    private final MyWxPayConfig config;
    private final SignType signType;
    private final boolean autoReport;
    private final boolean useSandbox;
    private final WXPayRequest wxPayRequest;

    public WXPay(final MyWxPayConfig config) {
        this(config, true, false);
    }

    public WXPay(final MyWxPayConfig config, final boolean autoReport) {
        this(config, autoReport, false);
    }

    public WXPay(final MyWxPayConfig config, final boolean autoReport, final boolean useSandbox) {
        this.config = config;
        this.checkWXPayConfig();
        this.autoReport = autoReport;
        this.useSandbox = useSandbox;
        this.signType = config.getSignType();
        this.wxPayRequest = new WXPayRequest(config);
    }

    private void checkWXPayConfig() {
        if (this.config == null) {
            throw new IllegalArgumentException("config is null");
        }
        if (this.config.getAppID() == null || this.config.getAppID().trim().length() == 0) {
            throw new IllegalArgumentException("appid in config is empty");
        }
        if (this.config.getMchID() == null || this.config.getMchID().trim().length() == 0) {
            throw new IllegalArgumentException("appid in config is empty");
        }
        if (this.config.getCertStream() == null) {
            throw new IllegalArgumentException("cert stream in config is empty");
        }
        if (this.config.getWXPayDomain() == null) {
            throw new IllegalArgumentException("config.getWXPayDomain() is null");
        }
        if (this.config.getHttpConnectTimeoutMs() < 10) {
            throw new IllegalArgumentException("http connect timeout is too small");
        }
        if (this.config.getHttpReadTimeoutMs() < 10) {
            throw new IllegalArgumentException("http read timeout is too small");
        }
    }

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     * @param reqData reqData
     * @return 封装参数后的Map
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) {
        reqData.put("appid", config.getAppID());
        reqData.put("mch_id", config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        if (SignType.MD5.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.MD5);
        } else if (SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), this.signType));
        log.info("签名参数：{}", reqData);
        return reqData;
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData) {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }

    /**
     * 判断支付结果通知中的sign是否有效
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) {
        String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData != null) {
            signTypeInData = signTypeInData.trim();
            if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            } else {
                signType = SignType.MD5;
            }
        } else {
            signType = SignType.MD5;
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }

    /**
     * 不需要证书的请求
     *
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     */
    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
            int connectTimeoutMs,
            int readTimeoutMs) {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);

        return this.wxPayRequest
                .requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs,
                        autoReport);
    }

    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData) {
        return this.requestWithoutCert(urlSuffix, reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 需要证书的请求
     *
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据 Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     */
    public String requestWithCert(String urlSuffix, Map<String, String> reqData,
            int connectTimeoutMs,
            int readTimeoutMs) {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);
        return this.wxPayRequest
                .requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs,
                        this.autoReport);
    }

    public String requestWithCert(String urlSuffix, Map<String, String> reqData) {
        return this.requestWithCert(urlSuffix, reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     *
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     */
    public Map<String, String> processResponseXml(String xmlStr) {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            throw new IllegalArgumentException(
                    String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(WXPayConstants.FAIL)) {
            return respData;
        } else if (return_code.equals(WXPayConstants.SUCCESS)) {
            if (this.isResponseSignatureValid(respData)) {
                return respData;
            } else {
                throw new IllegalArgumentException(
                        String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("return_code value %s is invalid in XML: %s", return_code,
                            xmlStr));
        }
    }

    /**
     * @param xmlStr xmlStr
     * @return 下单结果
     */
    public UnifiedOrderResult handleResponseXml(String xmlStr) {
        String return_code;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(WXPayConstants.RETURN_CODE)) {
            return_code = respData.get(WXPayConstants.RETURN_CODE);
        } else {
            throw new IllegalArgumentException(
                    String.format("No `return_code` in XML: %s", xmlStr));
        }
        if (return_code.equals(WXPayConstants.FAIL)) {
            return UnifiedOrderResult.error(respData);
        } else if (return_code.equals(WXPayConstants.SUCCESS)) {
            if (this.isResponseSignatureValid(respData)) {
                String result_code = respData.get(WXPayConstants.RESULT_CODE);
                if (result_code.equals(WXPayConstants.SUCCESS)) {
                    String timeStamp = String.valueOf(WXPayUtil.getCurrentTimestamp());
                    String signType = this.config.getSignType().getValue();
                    UnifiedOrderResult result = UnifiedOrderResult.ok();
                    String prepayId = respData.get("prepay_id");
                    result.setPrepayId(prepayId);
                    String packageStr = "prepay_id=" + prepayId;
                    String nonceStr = respData.get("nonce_str");
                    String appId = respData.get("appid");
                    result.setAppId(appId);
                    String mchId = respData.get("mch_id");
                    result.setMchId(mchId);
                    result.setNonceStr(nonceStr);
                    result.setTimeStamp(timeStamp);
                    result.setPackage(packageStr);
                    result.setSignType(signType);
                    String sign = WXPayUtil.generateSignature(result.map(), this.config.getKey(),
                            SignType.valueOf(signType));
                    result.setPaySign(sign);
                    return result;
                } else {
                    return UnifiedOrderResult.error(respData);
                }
            } else {
                throw new IllegalArgumentException(
                        String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("return_code value %s is invalid in XML: %s", return_code,
                            xmlStr));
        }
    }

    public UnifiedOrderResult rebuild(String packageStr) {
        UnifiedOrderResult result = UnifiedOrderResult.ok();
        result.setAppId(this.config.getAppID());
        result.setNonceStr(WXPayUtil.generateNonceStr());
        result.setTimeStamp(String.valueOf(WXPayUtil.getCurrentTimestamp()));
        result.setPackage(packageStr);
        result.setSignType(signType.getValue());
        String sign = WXPayUtil.generateSignature(result.map(), this.config.getKey(), signType);
        result.setPaySign(sign);
        return result;
    }

    /**
     * 作用：提交刷卡支付<br>
     * 场景：刷卡支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> microPay(Map<String, String> reqData) {
        return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：提交刷卡支付<br>
     * 场景：刷卡支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_MICROPAY_URL_SUFFIX;
        } else {
            url = WXPayConstants.MICROPAY_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 提交刷卡支付，针对软POS，尽可能做成功 内置重试机制，最多60s
     *
     * @param reqData reqData
     * @return 提交结果
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData) {
        return this.microPayWithPos(reqData, this.config.getHttpConnectTimeoutMs());
    }

    /**
     * 提交刷卡支付，针对软POS，尽可能做成功 内置重试机制，最多60s
     *
     * @param reqData reqData
     * @param connectTimeoutMs connectTimeoutMs
     * @return 提交结果
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) {
        int remainingTimeMs = 60 * 1000;
        long startTimestampMs;
        Map<String, String> lastResult = null;
        Exception lastException = null;

        while (true) {
            startTimestampMs = WXPayUtil.getCurrentTimestampMs();
            int readTimeoutMs = remainingTimeMs - connectTimeoutMs;
            if (readTimeoutMs > 1000) {
                try {
                    lastResult = this.microPay(reqData, connectTimeoutMs, readTimeoutMs);
                    String returnCode = lastResult.get(WXPayConstants.RETURN_CODE);
                    if (returnCode.equals(WXPayConstants.SUCCESS)) {
                        String resultCode = lastResult.get(WXPayConstants.RESULT_CODE);
                        String errCode = lastResult.get(WXPayConstants.ERR_CODE);
                        if (resultCode.equals(WXPayConstants.SUCCESS)) {
                            break;
                        } else {
                            // 看错误码，若支付结果未知，则重试提交刷卡支付
                            if (errCode.equals("SYSTEMERROR") || errCode.equals("BANKERROR")
                                    || errCode.equals("USERPAYING")) {
                                remainingTimeMs = remainingTimeMs
                                        - (int) (WXPayUtil.getCurrentTimestampMs()
                                        - startTimestampMs);
                                if (remainingTimeMs <= 100) {
                                    break;
                                } else {
                                    WXPayUtil.getLogger()
                                            .info("microPayWithPos: try micropay again");
                                    if (remainingTimeMs > 5000) {
                                        Thread.sleep(5000);
                                    } else {
                                        Thread.sleep(1000);
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    lastResult = null;
                    lastException = ex;
                }
            } else {
                break;
            }
        }
        if (lastResult == null && lastException != null) {
            throw new IllegalArgumentException(lastException);
        } else {
            return lastResult;
        }
    }

    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public UnifiedOrderResult unifiedOrder(Map<String, String> reqData) {
        return this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public UnifiedOrderResult unifiedOrder(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
        } else {
            url = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        }
        reqData.computeIfAbsent(WXPayConstants.TRADE_TYPE,
                k -> this.config.getTradeType().getValue());
        String notifyUrl;
        if (StringUtils.hasText((notifyUrl = this.config.getNotifyUrl()))) {
            reqData.put(WXPayConstants.NOTIFY_URL, notifyUrl);
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.handleResponseXml(respXml);
    }

    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> orderQuery(Map<String, String> reqData) {
        return this.orderQuery(reqData, config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData          向wxpay post的请求数据 int
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        } else {
            url = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：撤销订单<br>
     * 场景：刷卡支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> reverse(Map<String, String> reqData) {
        return this.reverse(reqData, config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：撤销订单<br>
     * 场景：刷卡支付<br>
     * 其他：需要证书
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REVERSE_URL_SUFFIX;
        } else {
            url = WXPayConstants.REVERSE_URL_SUFFIX;
        }
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：关闭订单<br>
     * 场景：公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> closeOrder(Map<String, String> reqData) {
        return this.closeOrder(reqData, config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：关闭订单<br>
     * 场景：公共号支付、扫码支付、APP支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_CLOSEORDER_URL_SUFFIX;
        } else {
            url = WXPayConstants.CLOSEORDER_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> refund(Map<String, String> reqData) {
        return this.refund(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：需要证书
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REFUND_URL_SUFFIX;
        } else {
            url = WXPayConstants.REFUND_URL_SUFFIX;
        }
        String notifyUrl;
        if (StringUtils.hasText((notifyUrl = this.config.getRefundNotifyUrl()))) {
            reqData.put(WXPayConstants.NOTIFY_URL, notifyUrl);
        }
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：退款查询<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> refundQuery(Map<String, String> reqData) {
        return this.refundQuery(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：退款查询<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REFUNDQUERY_URL_SUFFIX;
        } else {
            url = WXPayConstants.REFUNDQUERY_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：对账单下载（成功时返回对账单数据，失败时返回XML格式数据）<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> downloadBill(Map<String, String> reqData) {
        return this.downloadBill(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：对账单下载<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     * 其中return_code为`SUCCESS`，data为对账单数据。
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return 经过封装的API返回数据
     */
    public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_DOWNLOADBILL_URL_SUFFIX;
        } else {
            url = WXPayConstants.DOWNLOADBILL_URL_SUFFIX;
        }
        String respStr = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs)
                .trim();
        Map<String, String> ret;
        // 出现错误，返回XML数据
        if (respStr.indexOf("<") == 0) {
            ret = WXPayUtil.xmlToMap(respStr);
        } else {
            // 正常返回csv数据
            ret = new HashMap<String, String>();
            ret.put(WXPayConstants.RETURN_CODE, WXPayConstants.SUCCESS);
            ret.put("return_msg", "ok");
            ret.put("data", respStr);
        }
        return ret;
    }

    /**
     * 作用：交易保障<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> report(Map<String, String> reqData) {
        return this.report(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：交易保障<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REPORT_URL_SUFFIX;
        } else {
            url = WXPayConstants.REPORT_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return WXPayUtil.xmlToMap(respXml);
    }

    /**
     * 作用：转换短链接<br>
     * 场景：刷卡支付、扫码支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> shortUrl(Map<String, String> reqData) {
        return this.shortUrl(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：转换短链接<br>
     * 场景：刷卡支付、扫码支付
     *
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读取超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_SHORTURL_URL_SUFFIX;
        } else {
            url = WXPayConstants.SHORTURL_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 作用：授权码查询OPENID接口<br>
     * 场景：刷卡支付
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) {
        return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(),
                this.config.getHttpReadTimeoutMs());
    }

    /**
     * 作用：授权码查询OPENID接口<br>
     * 场景：刷卡支付
     *
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs    读超时时间，单位是毫秒
     * @return API返回数据
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs,
            int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_AUTHCODETOOPENID_URL_SUFFIX;
        } else {
            url = WXPayConstants.AUTHCODETOOPENID_URL_SUFFIX;
        }
        String respXml = this
                .requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs,
                        readTimeoutMs);
        return this.processResponseXml(respXml);
    }

} // end class
