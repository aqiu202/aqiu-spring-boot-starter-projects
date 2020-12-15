package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.qlock.id.SimpleIdGeneratorFactory;
import com.github.aqiu202.ttl.data.StringTtlCache;
import com.github.aqiu202.ttl.data.impl.AbstractTtlCache;
import com.github.aqiu202.ttl.data.str.StringRedisCache;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <pre>QLockRegistrar</pre>
 *
 * @author aqiu 2020/12/8 13:15
 **/
public class QLockConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String QLOCK_BEAN_NAME = "qLock";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        final AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(EnableQLock.class.getName()));
        final LockMode lockMode = attributes.getEnum("lockMode");
        final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                .genericBeanDefinition(lockMode.getLockClass());
        if (lockMode.isHasIdGenerator()) {
            final IdType idType = attributes.getEnum("idType");
            if (!IdType.AUTO.equals(idType)) {
                bdb.addPropertyValue("idGeneratorFactory", new SimpleIdGeneratorFactory<>(idType));
            } else {
                bdb.addAutowiredProperty("idGenerator");
            }
        }
        if (LockMode.zookeeper.equals(lockMode)) {
            bdb.addAutowiredProperty("curatorFramework");
        } else {
            final boolean otherCaching = attributes.getBoolean("otherCaching");
            long timeout = attributes.getNumber("timeout");
            TimeUnit timeUnit = attributes.getEnum("timeUnit");
            final CacheMode cacheMode = lockMode.getCacheMode();
            if (otherCaching) {
                bdb.addPropertyValue("cache",
                        this.createOtherCache(((BeanFactory) registry),
                                cacheMode.getStringCacheClass(), timeout,
                                timeUnit));
            } else {
                String beanName = TtlCacheConfigRegistrar.SIMPLE_STRING_TTL_CACHE_BEAN_NAME;
                TtlCacheConfigRegistrar.registerStringTtlCacheBean(registry, beanName,
                        cacheMode.getStringCacheClass(), timeout, timeUnit);
                bdb.addPropertyReference("cache", beanName);
            }
        }
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, bdb.getBeanDefinition());
    }

    private StringTtlCache createOtherCache(BeanFactory beanFactory,
            Class<? extends StringTtlCache> type,
            long timeout, TimeUnit timeUnit) {
        try {
            StringTtlCache cache = type.newInstance();
            if (cache instanceof AbstractTtlCache) {
                AbstractTtlCache abstractTtlCache = (AbstractTtlCache) cache;
                abstractTtlCache.setTimeout(timeout);
                abstractTtlCache.setTimeUnit(timeUnit);
            }
            if (cache instanceof StringRedisCache) {
                final StringRedisCache redisCache = (StringRedisCache) cache;
                redisCache.setCache(new StringRedisTemplate(beanFactory.getBean(
                        RedisConnectionFactory.class)));
            }
            return cache;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("初始化StringTtlCache失败", e);
        }
    }

}
