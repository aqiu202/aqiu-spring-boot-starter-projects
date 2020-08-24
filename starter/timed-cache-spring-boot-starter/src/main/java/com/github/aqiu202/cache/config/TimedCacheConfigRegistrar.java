package com.github.aqiu202.cache.config;


import com.github.aqiu202.cache.anno.EnableTimedCaching;
import com.github.aqiu202.cache.anno.EnableTimedCaching.CacheMode;
import com.github.aqiu202.cache.data.str.StringCaffeineCache;
import com.github.aqiu202.cache.data.str.StringGuavaCache;
import com.github.aqiu202.cache.data.str.StringRedisCache;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class TimedCacheConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableTimedCaching.class.getName(), false);
        if (map == null) {
            return;
        }
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(map);
        long timeout = attributes.getNumber("timeout");
        TimeUnit timeUnit = attributes.getEnum("timeUnit");
        CacheMode cacheMode = attributes.getEnum("cacheMode");
        GenericBeanDefinition b = new GenericBeanDefinition();
        if (CacheMode.redis.equals(cacheMode)) {
            b.setBeanClass(StringRedisCache.class);
            b.setAutowireCandidate(true);
        } else if (CacheMode.caffeine.equals(cacheMode)) {
            b.setBeanClass(StringCaffeineCache.class);
        } else if (CacheMode.guava.equals(cacheMode)) {
            b.setBeanClass(StringGuavaCache.class);
        }
        b.getPropertyValues().add("timeout", timeout);
        b.getPropertyValues().add("timeUnit", timeUnit);
        registry.registerBeanDefinition("simpleTimeLimitedStringCache", b);
    }

}
