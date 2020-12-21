package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.lock.base.AbstractReentrantTtlLock;
import com.github.aqiu202.lock.base.LockValueHolderStrategy;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.qlock.id.SimpleIdGeneratorFactory;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

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
        final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                .genericBeanDefinition(lockMode.getLockClass());
        if (lockMode.isHasIdGenerator()) {
            final IdType idType = attributes.getEnum("idType");
            if (!IdType.AUTO.equals(idType)) {
                bdb.addPropertyValue("idGeneratorFactory",
                        new SimpleIdGeneratorFactory<>((BeanFactory) registry, idType));
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
            String beanName = otherCaching ? QLOCK_STRING_TTL_CACHE_BEAN_NAME
                    : TtlCacheConfigRegistrar.SIMPLE_STRING_TTL_CACHE_BEAN_NAME;
            TtlCacheConfigRegistrar.registerStringTtlCacheBean(registry, beanName,
                    cacheMode.getStringCacheClass(), timeout, timeUnit);
            bdb.addPropertyReference("cache", beanName);
            if(AbstractReentrantTtlLock.class.isAssignableFrom(lockMode.getLockClass())) {
                final LockValueHolderStrategy lockValueHolderStrategy = attributes
                        .getEnum("lockValueHolderStrategy");
                bdb.addPropertyValue("lockValueHolderStrategy", lockValueHolderStrategy);
            }
        }
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, bdb.getBeanDefinition());
    }

}
