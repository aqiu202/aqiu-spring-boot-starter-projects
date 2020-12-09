package com.github.aqiu202.id.factory;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.IdGeneratorFactory;
import com.github.aqiu202.id.generator.RedisIdGenerator;
import com.github.aqiu202.id.generator.SnowFlakeIdGenerator;
import com.github.aqiu202.id.prop.IdProperties;
import com.github.aqiu202.id.type.IdType;
import com.github.aqiu202.id.prop.RedisIdProperties;
import com.github.aqiu202.id.prop.SnowFlakeIdProperties;
import java.io.Serializable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.NonNull;

/**
 * <pre>DefaultIdGeneratorFactory</pre>
 *
 * @author aqiu 2020/12/10 1:15
 **/
public class DefaultIdGeneratorFactory<T extends Serializable> implements IdGeneratorFactory<T>,
        ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final IdProperties properties;

    public DefaultIdGeneratorFactory(@NonNull IdProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public IdGenerator getIdGenerator() {
        final IdType type = this.properties.getType();
        IdGenerator idGenerator;
        try {
            idGenerator = type.getClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("ID Generator实例化失败", e);
        }
        if (idGenerator instanceof SnowFlakeIdGenerator) {
            final SnowFlakeIdProperties snowFlake = this.properties.getSnowFlake();
            ((SnowFlakeIdGenerator) idGenerator).configure(snowFlake);
        } else if (idGenerator instanceof RedisIdGenerator) {
            final RedisIdProperties redis = this.properties.getRedis();
            final RedisIdGenerator redisIdGenerator = (RedisIdGenerator) idGenerator;
            redisIdGenerator.configure(redis);
            redisIdGenerator.setConnectionFactory(
                    this.applicationContext.getBean(RedisConnectionFactory.class));
            redisIdGenerator.afterPropertiesSet();
        }
        return idGenerator;
    }
}
