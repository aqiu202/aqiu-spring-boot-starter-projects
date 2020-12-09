package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.qlock.id.SimpleIdGeneratorFactory;
import java.util.Map;
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

    public static final String QLOCK_BEAN_NAME = "qLockBean";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        final Map<String, Object> map = importingClassMetadata
                .getAnnotationAttributes(EnableQLock.class.getName(), false);
        final AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(map);
        final LockMode lockMode = annotationAttributes.getEnum("lockMode");
        final BeanDefinitionBuilder bdb = BeanDefinitionBuilder
                .genericBeanDefinition(lockMode.getLockClass());
        if(lockMode.isHasIdGenerator()) {
            final IdType idType = annotationAttributes.getEnum("idType");
            if(!IdType.AUTO.equals(idType)) {
                bdb.addPropertyValue("idGeneratorFactory", new SimpleIdGeneratorFactory<>(idType));
            } else {
                bdb.addAutowiredProperty("idGenerator");
            }
        }
        final boolean otherCaching = annotationAttributes.getBoolean("otherCaching");
        String beanName = otherCaching ? TtlLockCacheConfigRegistrar.SIMPLE_TTL_LOCK_CACHE_BEAN_NAME
                : TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME;
        bdb.addPropertyReference("cache", beanName);
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, bdb.getBeanDefinition());
    }
}
