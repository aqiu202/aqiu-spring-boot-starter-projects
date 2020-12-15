package com.github.aqiu202.qlock.id;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.IdGeneratorFactory;
import com.github.aqiu202.id.generator.RedisIdGenerator;
import com.github.aqiu202.id.type.IdType;
import java.io.Serializable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.NonNull;

/**
 * <pre>SimpleIdGeneratorFactory</pre>
 *
 * @author aqiu 2020/12/10 1:50
 **/
public class SimpleIdGeneratorFactory<T extends Serializable> implements IdGeneratorFactory<T> {

    private final BeanFactory beanFactory;

    private final IdType idType;

    public SimpleIdGeneratorFactory(BeanFactory beanFactory, IdType idType) {
        this.beanFactory = beanFactory;
        this.idType = idType;
    }

    @NonNull
    @Override
    public IdGenerator<T> getIdGenerator() {
        IdGenerator idGenerator;
        try {
            idGenerator = this.idType.getClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("ID Generator实例化失败", e);
        }
        if (idGenerator instanceof RedisIdGenerator) {
            final RedisIdGenerator redisIdGenerator = (RedisIdGenerator) idGenerator;
            redisIdGenerator.setKey(this.getClass().getName());
            redisIdGenerator.setConnectionFactory(
                    this.beanFactory.getBean(RedisConnectionFactory.class));
            redisIdGenerator.afterPropertiesSet();
        }
        return idGenerator;
    }

    @Override
    public IdType getIdType() {
        return this.idType;
    }
}
