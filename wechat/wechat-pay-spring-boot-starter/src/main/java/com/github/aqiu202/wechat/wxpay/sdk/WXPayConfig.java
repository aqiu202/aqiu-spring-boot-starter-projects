package com.github.aqiu202.wechat.wxpay.sdk;


import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.SignType;
import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.TradeType;
import java.io.InputStream;

public interface WXPayConfig {


    /**
     * 获取 App ID
     *
     * @return App ID
     */
    String getAppID();


    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    String getMchID();


    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    String getKey();

    /**
     * 签名加密方式
     * @return 签名加密方式
     */
    default SignType getSignType() {
        return SignType.MD5;
    }

    /**
     * 交易类型
     * @return 交易类型
     */
    default TradeType getTradeType() {
        return TradeType.JSAPI;
    }

    default String getNotifyUrl() {
        return null;
    }

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    InputStream getCertStream();

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return 连接超时时间，单位毫秒
     */
    default int getHttpConnectTimeoutMs() {
        return 6 * 1000;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return 读数据超时时间，单位毫秒
     */
    default int getHttpReadTimeoutMs() {
        return 8 * 1000;
    }

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     * @return WXPayDomain, 用于多域名容灾自动切换
     */
    IWXPayDomain getWXPayDomain();

    /**
     * 是否自动上报。
     * 若要关闭自动上报，子类中实现该函数返回 false 即可。
     *
     * @return 是否自动上报
     */
    default boolean shouldAutoReport() {
        return true;
    }

    /**
     * 进行健康上报的线程的数量
     *
     * @return 进行健康上报的线程的数量
     */
    default int getReportWorkerNum() {
        return 6;
    }


    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     *
     * @return 健康上报缓存消息的最大数量
     */
    default int getReportQueueMaxSize() {
        return 10000;
    }

    /**
     * 批量上报，一次最多上报多个数据
     *
     * @return 批量上报的个数
     */
    default int getReportBatchSize() {
        return 10;
    }

}
