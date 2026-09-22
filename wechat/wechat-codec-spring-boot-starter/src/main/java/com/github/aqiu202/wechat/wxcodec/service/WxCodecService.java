package com.github.aqiu202.wechat.wxcodec.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

public interface WxCodecService {

    JsonNode decrypt(String encryptedData, String sessionKey, String iv);

    ObjectNode decodeUserInfoByCode(String encryptedData, String iv, String code);

    JsonNode login(String code);

    JsonNode getPhoneNumber(String code);

    JsonNode getPhoneNumber(String code, String accessToken);

    String decodeRefundInfo(String content, String apiKey);

    JsonNode obtainAccessToken();

    JsonNode obtainAccessToken(String appid, String appSecret);

    JsonNode webLogin(String code);

    JsonNode webLogin(String appid, String secret, String code);

    JsonNode obtainGzhUserInfo(String accessToken, String openid);
}
