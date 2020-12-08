package com.github.aqiu202.huawei.sms.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aqiu202.api.sms.exp.SmsException;
import com.github.aqiu202.api.sms.param.SmsRequest;
import com.github.aqiu202.huawei.sms.bean.HwSmsProperty;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class DefaultHwSmsTemplate implements HwSmsTemplate {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String SUCCESS = "000000";
    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    public DefaultHwSmsTemplate(HwSmsProperty hwSmsProperty) {
        this.smsProperty = hwSmsProperty;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HwSmsProperty smsProperty;

    @Override
    public void sendMsg(SmsRequest smsRequest) {
        //必填,请参考"开发准备"获取如下数据,替换为实际值
        try {
            String url = smsProperty.getUrl(); //APP接入地址+接口访问URI
            String appKey = smsProperty.getAppKey(); //APP_Key
            String appSecret = smsProperty.getAppSecret(); //APP_Secret
            String sender = smsProperty.getSender(); //国内短信签名通道号或国际/港澳台短信通道号
            String templateId = smsProperty.getTemplates()
                    .get(smsRequest.getTemplateType()); //登录的模板ID

            //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
            //国际/港澳台短信不用关注该参数
            String signature = smsRequest.getSignName(); //签名名称
            if (StringUtils.isEmpty(signature)) {
                signature = smsProperty.getSignature();
            }
            //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
            String receiver = String.join(",", smsRequest.getPhoneNumbers()); //短信接收人号码
            //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
            String statusCallBack = smsProperty.getStatusCallBack();
            Map<String, String> mapParams = smsRequest.getParams();
            List<String> params = new ArrayList<>();
            mapParams.forEach((k, v) -> params.add(v));
            String templateParas = objectMapper.writeValueAsString(params); //模板变量
            //请求Body,不携带签名名称时,signature请填null
            String body = buildRequestBody(sender, receiver, templateId, templateParas,
                    statusCallBack,
                    signature);
            Assert.hasText(body, "body不能为空");
            //请求Headers中的X-WSSE参数值
            String wsseHeader = buildWsseHeader(appKey, appSecret);
            Assert.hasText(wsseHeader, "wsse header不能为空");
            //如果JDK版本低于1.8,可使用如下代码
            //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
            //CloseableHttpClient client = HttpClients.custom()
            //        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            //            @Override
            //            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //                return true;
            //            }
            //        }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            //如果JDK版本是1.8,可使用如下代码
            //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                            (x509CertChain, authType) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            CloseableHttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
            String string = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JsonNode root = objectMapper.readTree(string);
                String code = root.get("code").asText();
                if (!SUCCESS.equals(code)) {
                    throw new SmsException(string);
                }
            } else {
                throw new SmsException(string);
            }
        } catch (NoSuchAlgorithmException | IOException | KeyStoreException | KeyManagementException e) {
            log.error("华为短信服务调用失败：", e);
            throw new SmsException("华为短信服务调用失败", e);
        }
    }

    /**
     * 构造请求Body体
     * @param sender sender
     * @param receiver receiver
     * @param templateId templateId
     * @param templateParas templateParas
     * @param statusCallbackUrl statusCallbackUrl
     * @param signature | 签名名称,使用国内短信通用模板时填写
     */
    private String buildRequestBody(String sender, String receiver, String templateId,
            String templateParas,
            String statusCallbackUrl, String signature) {
        Assert.hasText(sender, "sender不能为空");
        Assert.hasText(receiver, "receiver不能为空");
        Assert.hasText(templateId, "templateId不能为空");
        List<NameValuePair> keyValues = new ArrayList<>();
        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }
        return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
    }

    /**
     * 构造X-WSSE参数值
     * @param appKey appKey
     * @param appSecret appSecret
     */
    private String buildWsseHeader(String appKey, String appSecret) {
        Assert.hasText(appKey, "appKey不能为空");
        Assert.hasText(appSecret, "appSecret不能为空");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce
        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder()
                .encodeToString(hexDigest.getBytes()); //PasswordDigest
        passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    @Override
    public void sendMsg(String templateType, String phoneNo, Map<String, String> params)
            throws SmsException {
        this.sendMsg(SmsRequest.of(templateType, phoneNo).setParams(params));
    }

    @Override
    public void sendMsg(String templateType, Collection<String> phoneNoList,
            Map<String, String> params) throws SmsException {
        this.sendMsg(SmsRequest.of(templateType, phoneNoList).setParams(params));
    }
}
