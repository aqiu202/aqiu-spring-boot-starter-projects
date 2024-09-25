package com.github.aqiu202.wechat.wxcodec.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface WxCodecService {

    JsonNode decrypt(String encryptedData, String sessionKey, String iv);

    ObjectNode decodeUserInfoByCode(String encryptedData, String iv, String code);

    JsonNode login(String code);

    JsonNode getPhoneNumber(String code);

    JsonNode getPhoneNumber(String code, String accessToken);

    String decodeRefundInfo(String content, String apiKey);

    JsonNode obtainGzhUserInfo(String accessToken, String openid);

    JsonNode obtainAccessToken();

    JsonNode gzhLogin(String code);

    JsonNode gzhLogin(String appid, String secret, String code);
}
