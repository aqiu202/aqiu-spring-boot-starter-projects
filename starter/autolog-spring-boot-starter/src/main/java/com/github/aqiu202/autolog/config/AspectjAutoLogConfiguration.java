package com.github.aqiu202.autolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aqiu202.autolog.aop.AopLogger;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AopLogger aopLogger(AutoLogConfigurationBean cb) {
        return new AopLogger(cb);
    }

}
