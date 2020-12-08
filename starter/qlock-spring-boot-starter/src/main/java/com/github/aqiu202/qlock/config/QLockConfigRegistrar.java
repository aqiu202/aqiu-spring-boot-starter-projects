package com.github.aqiu202.qlock.config;

import com.github.aqiu202.cache.config.TtlCacheConfigRegistrar;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import java.util.Map;
import org.springframework.beans.factory.config.RuntimeBeanReference;
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

    public static final String QLOCK_BEAN_NAME = "qLockBean";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        final Map<String, Object> map = importingClassMetadata
                .getAnnotationAttributes(EnableQLock.class.getName(), false);
        final AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(map);
        final LockMode lockMode = annotationAttributes.getEnum("lockMode");
        final GenericBeanDefinition qLockBean = new GenericBeanDefinition();
        qLockBean.setBeanClass(lockMode.getLockClass());
        qLockBean.getPropertyValues().add("cache", new RuntimeBeanReference(TtlCacheConfigRegistrar.SIMPLE_TTL_CACHE_BEAN_NAME));
        qLockBean.setAutowireCandidate(true);
        registry.registerBeanDefinition(QLOCK_BEAN_NAME, qLockBean);
    }
}
