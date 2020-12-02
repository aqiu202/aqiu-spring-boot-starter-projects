package com.github.aqiu202.limit.config;

import com.github.aqiu202.lock.base.CacheLock;
import com.github.aqiu202.lock.centralize.TtlLocaleLock;
import com.github.aqiu202.ttl.data.StringTtlCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TtlCacheLockConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CacheLock simpleCacheableLock(StringTtlCache stringTtlCache) {
        return new TtlLocaleLock(stringTtlCache);
    }
}
