package com.github.aqiu202.id.prop;

import com.github.aqiu202.id.type.IdType;
import java.security.SecureRandom;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <pre>ID Generator的配置信息</pre>
 *
 * @author aqiu 2020/12/3 12:45
 **/
@ConfigurationProperties(prefix = "id-generator")
public class IdProperties {

    private IdType type = IdType.SNOWFLAKE;

    @NestedConfigurationProperty
    private SnowFlakeIdProperties snowFlake = new SnowFlakeIdProperties();

    @NestedConfigurationProperty
    private RedisIdProperties redis = new RedisIdProperties();

    public IdType getType() {
        return type;
    }

    public void setType(IdType type) {
        this.type = type;
    }

    public SnowFlakeIdProperties getSnowFlake() {
        return snowFlake;
    }

    public void setSnowFlake(SnowFlakeIdProperties snowFlake) {
        this.snowFlake = snowFlake;
    }

    public RedisIdProperties getRedis() {
        return redis;
    }

    public void setRedis(RedisIdProperties redis) {
        this.redis = redis;
    }

    private static long randomNumber() {
        return new SecureRandom().nextInt(31);
    }
}
