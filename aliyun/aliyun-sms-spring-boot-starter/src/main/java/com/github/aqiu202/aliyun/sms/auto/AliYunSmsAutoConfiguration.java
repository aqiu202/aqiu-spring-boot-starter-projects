package com.github.aqiu202.aliyun.sms.auto;

import com.github.aqiu202.aliyun.sms.bean.AliYunSmsProperty;
import com.github.aqiu202.aliyun.sms.template.AliYunSmsTemplate;
import com.github.aqiu202.aliyun.sms.template.DefaultAliYunSmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "aliyun.mns", name = {"access-key-id", "access-key-secret"})
@EnableConfigurationProperties({AliYunSmsProperty.class})
public class AliYunSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliYunSmsTemplate aliYunSmsTemplate(AliYunSmsProperty aliYunSmsProperty) {
        return new DefaultAliYunSmsTemplate(aliYunSmsProperty);
    }

}
