package com.github.aqiu202.aliyun.tts.store.generator;

public class DefaultKeyGenerator implements KeyGenerator {

    @Override
    public String getKey(String accessKeyId) {
        return accessKeyId;
    }
}
