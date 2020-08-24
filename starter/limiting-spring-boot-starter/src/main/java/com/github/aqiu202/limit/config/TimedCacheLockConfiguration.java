package com.github.aqiu202.limit.config;

import com.github.aqiu202.cache.data.StringTimeLimitedCache;
import com.github.aqiu202.limit.lock.CacheableLock;
import com.github.aqiu202.limit.lock.impl.TimeLimitedLocaleLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TimedCacheLockConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CacheableLock simpleCacheableLock(StringTimeLimitedCache stringTimeLimitedCache) {
        return new TimeLimitedLocaleLock(stringTimeLimitedCache);
    }
}
