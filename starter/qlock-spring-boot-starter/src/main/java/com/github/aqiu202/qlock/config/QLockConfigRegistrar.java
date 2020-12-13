package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.qlock.id.SimpleIdGeneratorFactory;
import com.github.aqiu202.ttl.data.impl.AbstractTtlCache;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <pre>QLockRegistrar</pre>
 *
 * @author aqiu 2020/12/8 13:15
 **/
public class QLockConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String SIMPLE_TTL_LOCK_CACHE_BEAN_NAME = "simpleTtlLockStringCacheBean";

    public static final String QLOCK_BEAN_NAME = "qLockBean";

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
            String beanName = otherCaching ? SIMPLE_TTL_LOCK_CACHE_BEAN_NAME
                    : TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME;
            if (!registry.containsBeanDefinition(beanName)) {
                CacheMode cacheMode = lockMode.getCacheMode();
                GenericBeanDefinition bd = new GenericBeanDefinition();
                bd.setAutowireCandidate(cacheMode.isAutowireCandidate());
                final Class<?> value = cacheMode.getValue();
                bd.setBeanClass(cacheMode.getValue());
                if (!otherCaching) {
                    bd.setPrimary(true);
                }
                if (AbstractTtlCache.class.isAssignableFrom(value)) {
                    long timeout = attributes.getNumber("timeout");
                    TimeUnit timeUnit = attributes.getEnum("timeUnit");
                    bd.getPropertyValues().add("timeout", timeout);
                    bd.getPropertyValues().add("timeUnit", timeUnit);
                }
                registry.registerBeanDefinition(beanName, bd);
            }
            bdb.addPropertyReference("cache", beanName);
        }
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, bdb.getBeanDefinition());
    }
}
