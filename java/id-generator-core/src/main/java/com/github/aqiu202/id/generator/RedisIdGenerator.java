package com.github.aqiu202.id.generator;

import com.github.aqiu202.id.IdGenerator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.lang.NonNull;

/**
 * <pre>RedisIdGenerator</pre>
 *
 * @author aqiu 2020/12/2 14:26
 **/
public class RedisIdGenerator implements IdGenerator<Long> {

    private final RedisAtomicLong counter;

    private long delta = 1;

    public RedisIdGenerator(String key, RedisConnectionFactory connectionFactory,
            long initialValue) {
        this.counter = new RedisAtomicLong(key, connectionFactory, initialValue);
    }

    public RedisIdGenerator(String key, RedisConnectionFactory connectionFactory) {
        this.counter = new RedisAtomicLong(key, connectionFactory);
    }

    @NonNull
    @Override
    public Long generateId() {
        return this.counter.addAndGet(this.delta);
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }
}
