package com.github.aqiu202.cache.config;


import com.github.aqiu202.cache.anno.EnableTtlCaching;
import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.TtlCache;
import com.github.aqiu202.ttl.data.impl.AbstractTtlCache;
import com.github.aqiu202.ttl.data.impl.RedisCache;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class TtlCacheConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String SIMPLE_STRING_TTL_CACHE_BEAN_NAME = "stringTtlCache";
    public static final String SIMPLE_TTL_CACHE_BEAN_NAME = "ttlCache";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableTtlCaching.class.getName());
        if (map == null) {
            return;
        }
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(map);
        CacheMode cacheMode = attributes.getEnum("cacheMode");
        if (CacheMode.none.equals(cacheMode)) {
            return;
        }
        long timeout = attributes.getNumber("timeout");
        TimeUnit timeUnit = attributes.getEnum("timeUnit");
        //注册TtlCache
        registerTtlCacheBean(registry, SIMPLE_TTL_CACHE_BEAN_NAME, cacheMode.getCacheClass(),
                timeout,
                timeUnit);
        //注册StringTtlCache
        registerStringTtlCacheBean(registry, SIMPLE_STRING_TTL_CACHE_BEAN_NAME,
                cacheMode.getStringCacheClass(),
                timeout, timeUnit);
    }

    private static void setTimeout(BeanDefinitionBuilder builder, long timeout, TimeUnit timeUnit) {
        if (AbstractTtlCache.class.isAssignableFrom(builder.getBeanDefinition().getBeanClass())) {
            builder.addPropertyValue("timeout", timeout);
            builder.addPropertyValue("timeUnit", timeUnit);
        }
    }

    private static void setRedisTemplate(BeanDefinitionBuilder builder) {
        if (RedisCache.class.isAssignableFrom(builder.getBeanDefinition().getBeanClass())) {
            builder.addPropertyReference("cache", "redisTemplate");
        }
    }

    private static void setStringRedisTemplate(BeanDefinitionBuilder builder) {
        if (StringRedisCache.class.isAssignableFrom(builder.getBeanDefinition().getBeanClass())) {
            builder.addPropertyReference("cache", "stringRedisTemplate");
        }
    }

    public static void registerTtlCacheBean(BeanDefinitionRegistry registry, String name,
            Class<? extends TtlCache> type, long timeout,
            TimeUnit timeUnit) {
        if (!registry.containsBeanDefinition(name)) {
            final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                    .genericBeanDefinition(type);
            setTimeout(bdb, timeout, timeUnit);
            setRedisTemplate(bdb);
            registry.registerBeanDefinition(name, bdb.getBeanDefinition());
        }
    }

    public static void registerStringTtlCacheBean(BeanDefinitionRegistry registry, String name,
            Class<? extends StringTtlCache> type, long timeout,
            TimeUnit timeUnit) {
        if (!registry.containsBeanDefinition(name)) {
            final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                    .genericBeanDefinition(type);
            setTimeout(bdb, timeout, timeUnit);
            setStringRedisTemplate(bdb);
            registry.registerBeanDefinition(name, bdb.getBeanDefinition());
        }
    }

}
