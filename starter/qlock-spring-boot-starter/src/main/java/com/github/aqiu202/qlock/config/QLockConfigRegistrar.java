package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.lock.base.ReentrantCacheKeyLock;
import com.github.aqiu202.lock.base.LockValueStrategyMode;
import com.github.aqiu202.lock.redisson.AbstractRedissonKeyLock;
import com.github.aqiu202.lock.zk.AbstractZookeeperKeyLock;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.qlock.id.SimpleIdGeneratorFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.concurrent.TimeUnit;

/**
 * <pre>QLockRegistrar</pre>
 *
 * @author aqiu 2020/12/8 13:15
 **/
public class QLockConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String QLOCK_BEAN_NAME = "qLock";
    public static final String QLOCK_STRING_TTL_CACHE_BEAN_NAME = "qLockStringTtlCache";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        final AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(EnableQLock.class.getName()));
        final LockMode lockMode = attributes.getEnum("lockMode");
        Class<? extends KeyLock> lockClass = lockMode.getLockClass();
        final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                .genericBeanDefinition(lockClass);
        if (lockMode.isHasIdGenerator()) {
            final IdType idType = attributes.getEnum("idType");
            if (!IdType.AUTO.equals(idType)) {
                bdb.addPropertyValue("idGeneratorFactory",
                        new SimpleIdGeneratorFactory((BeanFactory) registry, idType));
            } else {
                bdb.addAutowiredProperty("idGenerator");
            }
        }
        if (AbstractZookeeperKeyLock.class.isAssignableFrom(lockClass)) {
            bdb.addAutowiredProperty("curatorFramework");
        } else if (AbstractRedissonKeyLock.class.isAssignableFrom(lockClass)) {
            bdb.addAutowiredProperty("redissonClient");
        } else {
            final boolean otherCaching = attributes.getBoolean("otherCaching");
            long timeout = attributes.getNumber("timeout");
            TimeUnit timeUnit = attributes.getEnum("timeUnit");
            final CacheMode cacheMode = lockMode.getCacheMode();
            String beanName = otherCaching ? QLOCK_STRING_TTL_CACHE_BEAN_NAME
                    : TtlCacheConfigRegistrar.SIMPLE_STRING_TTL_CACHE_BEAN_NAME;
            TtlCacheConfigRegistrar.registerStringTtlCacheBean(registry, beanName,
                    cacheMode.getStringCacheClass(), timeout, timeUnit);
            bdb.addPropertyReference("cache", beanName);
            if (ReentrantCacheKeyLock.class.isAssignableFrom(lockClass)) {
                final LockValueStrategyMode mode = attributes
                        .getEnum("lockValueStrategyMode");
                bdb.addPropertyValue("lockValueStrategyMode", mode);
            }
        }
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, bdb.getBeanDefinition());
    }

}
