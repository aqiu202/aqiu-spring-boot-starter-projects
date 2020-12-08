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
            if (!registry
                    .containsBeanDefinition(TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME)) {
                CacheMode cacheMode = lockMode.getCacheMode();
                GenericBeanDefinition b = new GenericBeanDefinition();
                b.setAutowireCandidate(cacheMode.isAutowireCandidate());
                final Class<?> value = cacheMode.getValue();
                b.setBeanClass(cacheMode.getValue());
                if (AbstractTtlCache.class.isAssignableFrom(value)) {
                    long timeout = attributes.getNumber("timeout");
                    TimeUnit timeUnit = attributes.getEnum("timeUnit");
                    b.getPropertyValues().add("timeout", timeout);
                    b.getPropertyValues().add("timeUnit", timeUnit);
                }
                registry.registerBeanDefinition(TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME,
                        b);
            }
        }
    }

}
