package com.github.aqiu202.aliyun.tts.store;

import com.alibaba.nls.client.AccessToken;
import com.github.aqiu202.aliyun.tts.store.generator.DefaultKeyGenerator;
import com.github.aqiu202.aliyun.tts.store.generator.KeyGenerator;
import com.github.aqiu202.ttl.data.StringTtlCache;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCacheableTokenStore implements CacheableTokenStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final KeyGenerator keyGenerator;

    protected final StringTtlCache cache;

    protected SimpleCacheableTokenStore(KeyGenerator keyGenerator, StringTtlCache cache) {
        this.keyGenerator = keyGenerator;
        this.cache = cache;
    }

    protected SimpleCacheableTokenStore(StringTtlCache cache) {
        this.keyGenerator = new DefaultKeyGenerator();
        this.cache = cache;
    }

    @Override
    public synchronized String getToken(String accessKeyId, String accessKeySecret) {
        String key = this.keyGenerator.getKey(accessKeyId);
        String token = this.getCache().get(key);
        if (Objects.isNull(token)) {
            AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
            try {
                accessToken.apply();
            } catch (Exception e) {
                log.error("缓存获取token失败：", e);
            }
            token = accessToken.getToken();
            if (Objects.nonNull(token)) {
                //空留10秒时间容错
                long expired =
                        accessToken.getExpireTime() - (System.currentTimeMillis() / 1000) - 10;
                this.getCache().set(key, token, expired, TimeUnit.SECONDS);
            }
        }
        return token;
    }

    @Override
    public boolean isExpired(String accessKeyId) {
        String key = this.keyGenerator.getKey(accessKeyId);
        return this.getCache().exists(key);
    }

    @Override
    public StringTtlCache getCache() {
        return this.cache;
    }
}
