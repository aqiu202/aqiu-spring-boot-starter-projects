package com.github.aqiu202.aliyun.tts.store;

public interface TokenStore {

    String getToken(String accessKeyId, String accessKeySecret);

    boolean isExpired(String accessKeyId);
}
