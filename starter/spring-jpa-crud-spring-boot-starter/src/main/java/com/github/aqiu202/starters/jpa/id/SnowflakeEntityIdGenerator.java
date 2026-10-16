package com.github.aqiu202.starters.jpa.id;

import com.github.aqiu202.id.generator.SnowFlakeIdGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

public class SnowflakeEntityIdGenerator implements BeforeExecutionGenerator {

    private static CustomKeyGenerator customKeyGenerator;

    private static final SnowFlakeIdGenerator snowFlakeIdGenerator = new SnowFlakeIdGenerator();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        // 已赋值的主键直接使用（配合 allowAssignedIdentifiers 支持外部赋值）
        if (currentValue != null) {
            return currentValue;
        }
        if (customKeyGenerator != null) {
            return customKeyGenerator.generate(owner, currentValue);
        }
        return snowFlakeIdGenerator.nextId();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    /**
     * Hibernate 6.5+ 默认不允许 persist 前手动赋值主键（会判定为 detached 并抛出
     * PersistentObjectException），此生成器显式支持外部赋值优先、否则自动生成
     */
    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }

    public static void setCustomKeyGenerator(CustomKeyGenerator customKeyGenerator) {
        SnowflakeEntityIdGenerator.customKeyGenerator = customKeyGenerator;
    }
}
