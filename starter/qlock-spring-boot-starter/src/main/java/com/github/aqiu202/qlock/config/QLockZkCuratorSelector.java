package com.github.aqiu202.qlock.config;

import com.github.aqiu202.lock.zk.AbstractZookeeperKeyLock;
import com.github.aqiu202.qlock.anno.EnableQLock;
import com.github.aqiu202.qlock.anno.EnableQLock.LockMode;
import com.github.aqiu202.zk.config.CuratorAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <pre>QLockZkCuratorSelector</pre>
 *
 * @author aqiu 2020/12/11 10:17
 **/
public class QLockZkCuratorSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        final AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata
                .getAnnotationAttributes(EnableQLock.class.getName()));
        LockMode lockMode = attributes.getEnum("lockMode");
        if (AbstractZookeeperKeyLock.class.isAssignableFrom(lockMode.getLockClass())) {
            return new String[]{CuratorAutoConfiguration.class.getName()};
        }
        return new String[0];
    }

}
