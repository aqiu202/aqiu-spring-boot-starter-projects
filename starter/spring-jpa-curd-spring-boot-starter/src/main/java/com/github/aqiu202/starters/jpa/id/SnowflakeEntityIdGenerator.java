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
        if (customKeyGenerator != null) {
            return customKeyGenerator.generate(owner, currentValue);
        }
        return snowFlakeIdGenerator.nextId();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    public static void setCustomKeyGenerator(CustomKeyGenerator customKeyGenerator) {
        SnowflakeEntityIdGenerator.customKeyGenerator = customKeyGenerator;
    }
}
