package com.github.aqiu202.wechat.wxpay.bean;


import com.github.aqiu202.wechat.wxpay.sdk.IWXPayDomain;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConfig;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.SignType;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.TradeType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class MyWxPayConfig implements WXPayConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String CLASS_PATH_FLAG = "classpath:";

    private final String appID;
    private final String mchID;
    private final String key;
    private final byte[] certData;
    private final SignType signType;
    private final TradeType tradeType;
    private final String notifyUrl;
    private final String refundNotifyUrl;

    public MyWxPayConfig(WxPayProperty property) {
        this.appID = property.getAppID();
        this.mchID = property.getMchID();
        this.key = property.getKey();
        this.signType = property.getSignType();
        this.tradeType = property.getTradeType();
        this.notifyUrl = property.getNotifyUrl();
        this.refundNotifyUrl = property.getRefundNotifyUrl();
        String path = property.getCert();
        InputStream certStream = null;
        try {
            try {
                if (path.startsWith(CLASS_PATH_FLAG)) {
                    path = path.replace(CLASS_PATH_FLAG, "");
                    ClassPathResource classPathResource = new ClassPathResource(path);
                    certStream = classPathResource.getInputStream();
                } else {
                    certStream = new FileInputStream(new File(path));
                }
                // 获取文件流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int n;
                while ((n = certStream.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
                this.certData = out.toByteArray();
            } finally {
                if (certStream != null) {
                    certStream.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取 App ID
     *
     * @return App ID
     */
    @Override
    public String getAppID() {
        return this.appID;
    }

    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    @Override
    public String getMchID() {
        return this.mchID;
    }

    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     *
     * @return WXPayDomain
     */
    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
                if (ex != null) {
                    log.error("", ex);
                }
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("api.mch.weixin.qq.com", true);
            }
        };
    }

    /**
     * @return the signType
     */
    @Override
    public SignType getSignType() {
        return signType;
    }

    /**
     * @return the tradeType
     */
    public TradeType getTradeType() {
        return tradeType;
    }

    /**
     * @return the signType
     */
    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }
}
