package com.github.aqiu202.cache.data.str;


import com.github.aqiu202.cache.data.StringTimeLimitedCache;
import com.github.aqiu202.cache.data.impl.CaffeineCache;

public class StringCaffeineCache extends CaffeineCache<String, String> implements
        StringTimeLimitedCache {

    public StringCaffeineCache() {
    }

}
