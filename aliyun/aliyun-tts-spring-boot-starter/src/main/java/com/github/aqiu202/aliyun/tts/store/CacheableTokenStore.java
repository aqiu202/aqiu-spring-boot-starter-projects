package com.github.aqiu202.aliyun.tts.store;

import com.github.aqiu202.ttl.data.StringTtlCache;

public interface CacheableTokenStore extends TokenStore {

    StringTtlCache getCache();
}
