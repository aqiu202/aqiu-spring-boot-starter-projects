package com.github.aqiu202.huawei.sms.auto;

import com.github.aqiu202.huawei.sms.bean.HwSmsProperty;
import com.github.aqiu202.huawei.sms.template.DefaultHwSmsTemplate;
import com.github.aqiu202.huawei.sms.template.HwSmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HwSmsProperty.class)
public class HwSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HwSmsTemplate hwSmsTemplate(HwSmsProperty hwSmsProperty) {
        return new DefaultHwSmsTemplate(hwSmsProperty);
    }

}
