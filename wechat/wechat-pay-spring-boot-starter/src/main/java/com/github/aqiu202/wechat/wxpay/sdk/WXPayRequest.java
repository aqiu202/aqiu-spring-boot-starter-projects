package com.github.aqiu202.wechat.wxpay.sdk;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class WXPayRequest {

    private WXPayConfig config;

    public WXPayRequest(WXPayConfig config) {
        this.config = config;
    }

    /**
     * 请求，只请求一次，不做重试
     * @param domain domain
     * @param urlSuffix urlSuffix
     * @param data data
     * @param connectTimeoutMs connectTimeoutMs
     * @param readTimeoutMs readTimeoutMs
     * @param useCert 是否使用证书，针对退款、撤销等操作
     */
    private String requestOnce(final String domain, String urlSuffix, String data,
            int connectTimeoutMs, int readTimeoutMs, boolean useCert) {
        BasicHttpClientConnectionManager connManager;
        try {
            if (useCert) {
                // 证书
                char[] password = config.getMchID().toCharArray();
                InputStream certStream = config.getCertStream();
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(certStream, password);

                // 实例化密钥库 & 初始化密钥工厂
                KeyManagerFactory kmf = KeyManagerFactory
                        .getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, password);

                // 创建 SSLContext
                //            SSLContext sslContext = SSLContext.getInstance("TLS"); //NOSONAR
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                        sslContext,
                        new String[]{"TLSv1"},
                        null,
                        new DefaultHostnameVerifier());

                connManager = new BasicHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                                .register("https", sslConnectionSocketFactory)
                                .build(),
                        null,
                        null,
                        null
                );
            } else {
                connManager = new BasicHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                                .build(),
                        null,
                        null,
                        null
                );
            }

            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connManager)
                    .build();

            String url = "https://" + domain + urlSuffix;
            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs)
                    .setConnectTimeout(connectTimeoutMs).build();
            httpPost.setConfig(requestConfig);

            StringEntity postEntity = new StringEntity(data, StandardCharsets.UTF_8);
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT + " " + config.getMchID());
            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        } catch (KeyStoreException | KeyManagementException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    private String request(String urlSuffix, String uuid, String data, int connectTimeoutMs,
            int readTimeoutMs, boolean useCert, boolean autoReport) {
        Exception exception;
        long elapsedTimeMillis = 0;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        boolean firstHasDnsErr = false;
        boolean firstHasConnectTimeout = false;
        boolean firstHasReadTimeout = false;
        IWXPayDomain.DomainInfo domainInfo = config.getWXPayDomain().getDomain(config);
        if (domainInfo == null) {
            throw new IllegalArgumentException(
                    "WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }
        try {
            String result = requestOnce(domainInfo.getDomain(), urlSuffix, data,
                    connectTimeoutMs, readTimeoutMs, useCert);
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            if (autoReport) {
                config.getWXPayDomain().report(domainInfo.getDomain(), elapsedTimeMillis, null);
                WXPayReport.getInstance(config).report(
                        uuid,
                        elapsedTimeMillis,
                        domainInfo.getDomain(),
                        domainInfo.isPrimaryDomain(),
                        connectTimeoutMs,
                        readTimeoutMs,
                        firstHasDnsErr,
                        firstHasConnectTimeout,
                        firstHasReadTimeout);
            }
            return result;
        } catch (Exception ex) {
            exception = ex;
            if (autoReport) {
                elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
                WXPayReport.getInstance(config).report(
                        uuid,
                        elapsedTimeMillis,
                        domainInfo.getDomain(),
                        domainInfo.isPrimaryDomain(),
                        connectTimeoutMs,
                        readTimeoutMs,
                        firstHasDnsErr,
                        firstHasConnectTimeout,
                        firstHasReadTimeout);
            }
            config.getWXPayDomain().report(domainInfo.getDomain(), elapsedTimeMillis, exception);
        }
        throw new IllegalArgumentException(exception);
    }


    /**
     * 可重试的，非双向认证的请求
     * @param urlSuffix urlSuffix
     * @param uuid uuid
     * @param data data
     * @param autoReport 是否自动上报
     * @return 请求结果
     */
    public String requestWithoutCert(String urlSuffix, String uuid, String data,
            boolean autoReport) {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(),
                config.getHttpReadTimeoutMs(), false, autoReport);
    }

    /**
     * 可重试的，非双向认证的请求
     * @param urlSuffix urlSuffix
     * @param uuid uuid
     * @param data data
     * @param connectTimeoutMs connectTimeoutMs
     * @param readTimeoutMs readTimeoutMs
     * @param autoReport 是否自动上报
     * @return 请求结果
     */
    public String requestWithoutCert(String urlSuffix, String uuid, String data,
            int connectTimeoutMs, int readTimeoutMs, boolean autoReport) {
        return this
                .request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, false, autoReport);
    }

    /**
     * 可重试的，双向认证的请求
     * @param urlSuffix urlSuffix
     * @param uuid uuid
     * @param data data
     * @param autoReport 是否自动上报
     * @return 请求结果
     */
    public String requestWithCert(String urlSuffix, String uuid, String data, boolean autoReport) {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(),
                config.getHttpReadTimeoutMs(), true, autoReport);
    }

    /**
     * 可重试的，双向认证的请求
     * @param urlSuffix urlSuffix
     * @param uuid uuid
     * @param data data
     * @param connectTimeoutMs connectTimeoutMs
     * @param readTimeoutMs readTimeoutMs
     * @param autoReport 是否自动上报
     * @return 请求结果
     */
    public String requestWithCert(String urlSuffix, String uuid, String data, int connectTimeoutMs,
            int readTimeoutMs, boolean autoReport) {
        return this
                .request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, true, autoReport);
    }
}
