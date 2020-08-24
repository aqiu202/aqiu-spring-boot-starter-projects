package com.github.aqiu202.aliyun.tts.store;


import com.github.aqiu202.cache.data.StringTimeLimitedCache;

public interface CacheableTokenStore extends TokenStore {

    StringTimeLimitedCache getCache();
}
