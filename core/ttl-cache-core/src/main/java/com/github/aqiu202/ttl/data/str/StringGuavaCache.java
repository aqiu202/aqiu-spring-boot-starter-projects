package com.github.aqiu202.ttl.data.str;


import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.impl.GuavaCache;

public class StringGuavaCache extends GuavaCache<String, String> implements
        StringTtlCache {

    public StringGuavaCache() {
    }
}
