package com.github.aqiu202.id.prop;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.generator.RedisIdGenerator;
import com.github.aqiu202.id.generator.SimpleUUIDGenerator;
import com.github.aqiu202.id.generator.SnowFlakeIdGenerator;
import com.github.aqiu202.id.generator.UUIDGenerator;
import java.io.Serializable;

/**
 * <pre>注解生成策略类型</pre>
 *
 * @author aqiu 2020/12/3 12:47
 **/
public enum IdType {
    UUID(UUIDGenerator.class),
    SIMPLE_UUID(SimpleUUIDGenerator.class),
    SNOWFLAKE(SnowFlakeIdGenerator.class),
    REDIS(RedisIdGenerator.class);

    IdType(Class<? extends IdGenerator<? extends Serializable>> clazz) {
        this.clazz = clazz;
    }

    private final Class<? extends IdGenerator<? extends Serializable>> clazz;

    public Class<? extends IdGenerator<? extends Serializable>> getClazz() {
        return this.clazz;
    }
}
