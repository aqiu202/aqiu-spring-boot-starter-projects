package com.github.aqiu202.cache.data.str;

import com.github.aqiu202.cache.data.StringTimeLimitedCache;
import com.github.aqiu202.cache.data.impl.GuavaCache;

public class StringGuavaCache extends GuavaCache<String, String> implements
        StringTimeLimitedCache {

    public StringGuavaCache() {
    }
}
