package com.github.aqiu202.limit.config;

import com.github.aqiu202.lock.cache.CacheKeyLock;
import com.github.aqiu202.lock.cache.SimpleCacheKeyLock;
import com.github.aqiu202.ttl.data.StringTtlCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TtlCacheLockConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CacheKeyLock simpleCacheableLock(StringTtlCache stringTtlCache) {
        final SimpleCacheKeyLock simpleCacheLock = new SimpleCacheKeyLock();
        simpleCacheLock.setCache(stringTtlCache);
        return simpleCacheLock;
    }
}
