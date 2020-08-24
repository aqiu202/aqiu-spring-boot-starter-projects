package com.github.aqiu202.wechat.wxpay.sdk;

import org.apache.http.client.HttpClient;

/**
 * 常量
 */
public class WXPayConstants {

    public enum SignType {
        /**
         * MD5
         */
        MD5("MD5"),
        /**
         * HMAC-SHA256
         */
        HMACSHA256("HMAC-SHA256");
        private final String value;

        SignType(String v) {
            this.value = v;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return this.value;
        }

        public static SignType parse(String value) {
            if ("HMAC-SHA256".equals(value)) {
                return HMACSHA256;
            }
            return MD5;
        }
    }

    public enum TradeType {
        /**
         * 扫码支付
         */
        NATIVE("NATIVE"),
        /**
         * 公众号或者小程序支付
         */
        JSAPI("JSAPI"),
        /**
         * H5支付
         */
        MWEB("MWEB"),
        /**
         * APP支付
         */
        APP("APP");
        private final String value;

        TradeType(String v) {
            this.value = v;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return this.value;
        }
    }

    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";

    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    public static final String RETURN_CODE = "return_code";
    public static final String RESULT_CODE = "result_code";
    public static final String ERR_CODE = "err_code";
    public static final String NOTIFY_URL = "notify_url";
    public static final String TRADE_TYPE = "trade_type";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String WXPAYSDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT =
            WXPAYSDK_VERSION + " (" + System.getProperty("os.arch") + " "
                    + System.getProperty("os.name") + " " + System.getProperty("os.version")
                    + ") Java/"
                    + System.getProperty("java.version") + " HttpClient/"
                    + HttpClient.class.getPackage().getImplementationVersion();

    public static final String MICROPAY_URL_SUFFIX = "/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX = "/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX = "/pay/closeorder";
    public static final String REFUND_URL_SUFFIX = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX = "/payitil/report";
    public static final String SHORTURL_URL_SUFFIX = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";

    // sandbox
    public static final String SANDBOX_MICROPAY_URL_SUFFIX = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "/sandboxnew/tools/authcodetoopenid";

}
