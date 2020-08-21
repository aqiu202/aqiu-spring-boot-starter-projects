package com.github.aqiu202.aurora.jpush.config;


import com.github.aqiu202.aurora.jpush.bean.JPushBean;
import com.github.aqiu202.aurora.jpush.service.JPushService;
import com.github.aqiu202.aurora.jpush.service.JPushServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JPushBean.class)
public class JPushAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JPushService jPushService(JPushBean jPushBean) {
        return new JPushServiceImpl(jPushBean);
    }
}
