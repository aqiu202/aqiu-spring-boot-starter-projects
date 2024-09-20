package com.github.aqiu202.autolog.config;

import com.github.aqiu202.autolog.aop.AspectjAutoLogBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoLog配置类
 *
 * @author aqiu
 * @version 2018年10月24日 下午3:55:02
 */
@Configuration(proxyBeanMethods = false)
public class AspectjAutoLogConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AspectjAutoLogBean aopLogger(AutoLogConfigurationBean cb) {
        return new AspectjAutoLogBean(cb);
    }

}
