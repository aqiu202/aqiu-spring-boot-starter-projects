package com.github.aqiu202.qlock.config;


import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.ttl.data.impl.AbstractTtlCache;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class TtlLockCacheConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String SIMPLE_TTL_LOCK_CACHE_BEAN_NAME = "simpleTtlLockStringCacheBean";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableQLock.class.getName(), false);
        if (map == null) {
            return;
        }
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(map);
        LockMode lockMode = attributes.getEnum("lockMode");
        if (!LockMode.zookeeper.equals(lockMode)) {
            final boolean otherCaching = attributes.getBoolean("otherCaching");
            if (!otherCaching && registry
                    .containsBeanDefinition(TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME)) {
                return;
            }
            String beanName = otherCaching ? SIMPLE_TTL_LOCK_CACHE_BEAN_NAME
                    : TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME;
            CacheMode cacheMode = lockMode.getCacheMode();
            GenericBeanDefinition bd = new GenericBeanDefinition();
            bd.setAutowireCandidate(cacheMode.isAutowireCandidate());
            final Class<?> value = cacheMode.getValue();
            bd.setBeanClass(cacheMode.getValue());
            if(!otherCaching) {
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
    }

}
