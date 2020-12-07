package com.github.aqiu202.id.config;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.generator.RedisIdGenerator;
import com.github.aqiu202.id.generator.SnowFlakeIdGenerator;
import com.github.aqiu202.id.prop.IdProperties;
import com.github.aqiu202.id.prop.IdType;
import com.github.aqiu202.id.prop.RedisIdProperties;
import com.github.aqiu202.id.prop.SnowFlakeIdProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * <pre>Id Generator自动配置</pre>
 *
 * @author aqiu 2020/12/3 12:59
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(IdProperties.class)
public class IdGeneratorAutoConfiguration {

    @Bean
    public IdGenerator idGenerator(IdProperties idProperties, ApplicationContext context) {
        final IdType type = idProperties.getType();
        IdGenerator idGenerator;
        if (IdType.REDIS.equals(type)) {
            final RedisIdProperties redis = idProperties.getRedis();
            idGenerator = new RedisIdGenerator(redis.getKey(), context.getBean(
                    RedisConnectionFactory.class), redis.getInitValue());
        } else {
            try {
                idGenerator = type.getClazz().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("ID Generator实例化失败", e);
            }
            if (IdType.SNOWFLAKE.equals(type)) {
                final SnowFlakeIdProperties snowFlake = idProperties.getSnowFlake();
                final SnowFlakeIdGenerator snowFlakeIdGenerator = (SnowFlakeIdGenerator) idGenerator;
                snowFlakeIdGenerator.setWorkerId(snowFlake.getWorkerId());
                snowFlakeIdGenerator.setDataCenterId(snowFlake.getDataCenterId());
            }
        }
        return idGenerator;
    }
}
