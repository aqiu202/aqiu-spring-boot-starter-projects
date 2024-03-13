package com.github.aqiu202.qlock.cond;

import com.github.aqiu202.lock.redisson.AbstractRedissonKeyLock;
import com.github.aqiu202.qlock.anno.EnableQLock;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnRedissonCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(EnableQLock.class.getName()));
        EnableQLock.LockMode lockMode = attributes.getEnum("lockMode");
        if (AbstractRedissonKeyLock.class.isAssignableFrom(lockMode.getLockClass())) {
            return ConditionOutcome.match();
        }
        return ConditionOutcome.noMatch("未找到Zookeeper的配置");
    }
}
