package com.github.aqiu202.wechat.wxcodec.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.aqiu202.util.AESUtils;
import com.github.aqiu202.util.MD5Utils;
import com.github.aqiu202.util.StringUtils;
import com.github.aqiu202.wechat.wxcodec.bean.WxCodecProperty;
import com.github.aqiu202.wechat.wxcodec.encoder.WxPKCS7Encoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import org.springframework.web.client.RestTemplate;

public class DecryptServiceImpl implements DecryptService {

    private final WxCodecProperty wxCodecProperty;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public DecryptServiceImpl(WxCodecProperty wxCodecProperty) {
        this.wxCodecProperty = wxCodecProperty;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public JsonNode session(String code) {
        if (code == null || code.length() == 0) {
            throw new IllegalArgumentException("登录凭证不能为空");
        }
        // 授权（必填）
        String grant_type = "authorization_code";
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        // 请求参数
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={1}&secret={2}&js_code={3}&grant_type={4}";
        // 发送请求
        String str = restTemplate.getForObject(url, String.class,
                this.wxCodecProperty.getAppId(), this.wxCodecProperty.getSecret(), code,
                grant_type);
        // 解析相应内容（转换成json对象）
        if (StringUtils.hasText(str)) {
            try {
                return mapper.readTree(str);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public JsonNode decrypt(String encryptedData, String sessionKey, String iv) {
        String result = null;
        byte[] resultByte;
        final Decoder decoder = Base64.getDecoder();
        try {
            resultByte = AESUtils
                    .decrypt(decoder.decode(encryptedData), decoder.decode(sessionKey),
                            decoder.decode(iv));
            if (null != resultByte && resultByte.length > 0) {
                result = new String(WxPKCS7Encoder.decode(resultByte));
            }
            if (StringUtils.hasText(result)) {
                return mapper.readTree(result);
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public ObjectNode decodeUser(String encryptedData, String iv, String code) {
        JsonNode data = this.session(code);
        if (data != null) {
            JsonNode sessionKey;
            JsonNode openId;
            if ((sessionKey = data.get("session_key")) != null
                    && (openId = data.get("openid")) != null) {
                ObjectNode obj = mapper.createObjectNode();
                JsonNode res = this.decrypt(encryptedData, sessionKey.asText(), iv);
                obj.set("data", res);
                obj.put("openid", openId.asText());
                return obj;
            }
        }
        return null;
    }

    /**
     * 微信支付退款信息解密
     * @author aqiu 2019/11/1 3:38 下午
     * @param content: 密文
     * @param apiKey: 接口api秘钥
     * @return {@link String}
     **/
    @Override
    public String decodeRefundInfo(String content, String apiKey) {
        final Decoder decoder = Base64.getDecoder();
        String key = MD5Utils.encode(apiKey);
        byte[] bytes = AESUtils.decrypt(decoder.decode(content), key.getBytes());
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public JsonNode accessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token"
                + "?appid={1}&secret={2}&code={3}&grant_type=authorization_code";
        String str = this.restTemplate
                .getForObject(url, String.class,
                        this.wxCodecProperty.getAppId(),
                        this.wxCodecProperty.getSecret(), code);
        try {
            return this.mapper.readTree(str);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json解析失败，" + e.getMessage());
        }
    }

    @Override
    public JsonNode userInfo(String accessToken, String openid) {
        //获取资源信息
        String url = " https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken +
                "&openid=" + openid +
                "&lang=zh_CN";
        String str = this.restTemplate.getForObject(url, String.class);
        try {
            return this.mapper.readTree(str);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json解析失败，" + e.getMessage());
        }
    }

    @Override
    public JsonNode accessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={1}&secret={2}";
        return this.restTemplate
                .getForObject(url, JsonNode.class, this.wxCodecProperty.getAppId(),
                        this.wxCodecProperty.getSecret());
    }
}
