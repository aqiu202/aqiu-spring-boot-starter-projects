package com.github.aqiu202.cache.config;


import com.github.aqiu202.cache.anno.EnableTtlCaching;
import com.github.aqiu202.cache.anno.EnableTtlCaching.CacheMode;
import com.github.aqiu202.ttl.data.impl.AbstractTtlCache;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class TtlCacheConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String SIMPLE_TTL_CACHE_BEAN_NAME = "simpleTtlStringCacheBean";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableTtlCaching.class.getName(), false);
        if (map == null) {
            return;
        }
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(map);
        CacheMode cacheMode = attributes.getEnum("cacheMode");
        if(CacheMode.none.equals(cacheMode)) {
            return;
        }
        final Class<?> value = cacheMode.getValue();
        GenericBeanDefinition b = new GenericBeanDefinition();
        b.setAutowireCandidate(cacheMode.isAutowireCandidate());
        b.setBeanClass(value);
        b.setPrimary(true);
        if(AbstractTtlCache.class.isAssignableFrom(value)) {
            long timeout = attributes.getNumber("timeout");
            TimeUnit timeUnit = attributes.getEnum("timeUnit");
            final MutablePropertyValues propertyValues = b.getPropertyValues();
            propertyValues.add("timeout", timeout);
            propertyValues.add("timeUnit", timeUnit);
        }
        registry.registerBeanDefinition(SIMPLE_TTL_CACHE_BEAN_NAME, b);
    }

}
