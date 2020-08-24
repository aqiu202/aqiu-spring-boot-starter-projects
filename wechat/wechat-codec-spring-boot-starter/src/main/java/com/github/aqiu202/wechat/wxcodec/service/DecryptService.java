package com.github.aqiu202.wechat.wxcodec.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface DecryptService {

    JsonNode decrypt(String encryptedData, String sessionKey, String iv);

    JsonNode session(String code);

    ObjectNode decodeUser(String encryptedData, String iv, String code);

    String decodeRefundInfo(String content, String apiKey);

    JsonNode accessToken(String code);

    JsonNode userInfo(String accessToken, String openid);

    JsonNode accessToken();
}
