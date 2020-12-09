package com.github.aqiu202.id.generator;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.prop.RedisIdProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * <pre>RedisIdGenerator</pre>
 *
 * @author aqiu 2020/12/2 14:26
 **/
public class RedisIdGenerator implements IdGenerator<Long>, InitializingBean {

    private RedisAtomicLong counter;

    private RedisConnectionFactory connectionFactory;

    private String key;
    private long delta = 1;
    private long initialValue = 0;

    public RedisIdGenerator() {
    }

    public RedisIdGenerator(RedisIdProperties properties) {
        this.configure(properties);
    }

    public void configure(RedisIdProperties properties) {
        this.setKey(properties.getKey());
        this.setInitialValue(properties.getInitValue());
        this.setDelta(properties.getDelta());
    }

    public void setConnectionFactory(
            RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getDelta() {
        return delta;
    }

    public long getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(long initialValue) {
        this.initialValue = initialValue;
    }

    @NonNull
    @Override
    public Long nextId() {
        return this.counter.addAndGet(this.delta);
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.connectionFactory, "Redis ConnectionFactory不能为空");
        this.counter = new RedisAtomicLong(this.key, this.connectionFactory, this.initialValue);
    }
}
