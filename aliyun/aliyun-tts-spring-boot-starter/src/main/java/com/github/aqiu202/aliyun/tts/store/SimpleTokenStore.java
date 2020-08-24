package com.github.aqiu202.aliyun.tts.store;

import com.alibaba.nls.client.AccessToken;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTokenStore implements TokenStore {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getToken(String accessKeyId, String accessKeySecret) {
        String accessToken = null;
        try {
            AccessToken token = new AccessToken(accessKeyId, accessKeySecret);
            token.apply();
            accessToken = token.getToken();
        } catch (IOException e) {
            logger.error("获取token失败：", e);
        }
        return accessToken;
    }

    @Override
    public boolean isExpired(String accessKeyId) {
        return true;
    }

}
