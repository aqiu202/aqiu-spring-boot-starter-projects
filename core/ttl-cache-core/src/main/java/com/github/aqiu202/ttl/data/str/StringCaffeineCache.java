package com.github.aqiu202.ttl.data.str;


import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.impl.CaffeineCache;

public class StringCaffeineCache extends CaffeineCache<String, String> implements
        StringTtlCache {

    public StringCaffeineCache() {
    }

}
