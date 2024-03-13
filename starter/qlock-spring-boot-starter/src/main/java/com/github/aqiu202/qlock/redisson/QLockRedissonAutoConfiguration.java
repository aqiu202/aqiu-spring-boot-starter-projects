package com.github.aqiu202.qlock.redisson;

import com.github.aqiu202.qlock.cond.OnRedissonCondition;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Conditional(OnRedissonCondition.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedissonProperty.class)
public class QLockRedissonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient defaultRedissonClient(RedissonProperty property) {
        Config config = new Config();
        String single = property.getSingle();
        if (StringUtils.isNotBlank(single)) {
            config.useSingleServer()
                    .setAddress(single)
                    .setDatabase(property.getSingleDatabase());
        } else {
            List<String> nodes = property.getNodes();
            if (CollectionUtils.isEmpty(nodes)) {
                throw new RuntimeException("Redisson节点配置异常，未找到Redis的相关配置");
            }
            config.useClusterServers().setNodeAddresses(nodes);
        }
        config.setLockWatchdogTimeout(property.getLockWatchdogTimeout())
                .setUseScriptCache(property.isUseScriptCache())
                .setMinCleanUpDelay(property.getMinCleanUpDelay())
                .setMaxCleanUpDelay(property.getMaxCleanUpDelay())
                .setCleanUpKeysAmount(property.getCleanUpKeysAmount());
        return Redisson.create(config);
    }

}
