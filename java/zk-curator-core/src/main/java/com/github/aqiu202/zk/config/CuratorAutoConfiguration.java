package com.github.aqiu202.zk.config;

import com.github.aqiu202.zk.common.CuratorFrameworkCreator;
import com.github.aqiu202.zk.prop.CuratorProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>CuratorAutoConfiguration</pre>
 *
 * @author aqiu 2020/12/10 16:20
 **/
@ConditionalOnMissingBean(CuratorFramework.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RetryPolicy retryPolicy(CuratorProperties properties) {
        return new ExponentialBackoffRetry(properties.getBaseSleepTime(),
                properties.getMaxRetries(),
                properties.getMaxSleepTime());
    }

    @Bean
    public CuratorFramework curatorFramework(CuratorProperties properties,
            RetryPolicy retryPolicy) {
        return CuratorFrameworkCreator.createCuratorFramework(properties, retryPolicy);
    }

}
