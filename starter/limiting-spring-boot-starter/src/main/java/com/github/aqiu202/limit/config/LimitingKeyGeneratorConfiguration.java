package com.github.aqiu202.limit.config;

import com.github.aqiu202.aop.keygen.KeyGenerator;
import com.github.aqiu202.limit.key.SessionMethodKeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LimitingKeyGeneratorConfiguration {

    public static final String SESSION_METHOD_KEY_GENERATOR_NAME = "sessionMethodKeyGenerator";

    @Bean(SESSION_METHOD_KEY_GENERATOR_NAME)
    @ConditionalOnMissingBean(name = SESSION_METHOD_KEY_GENERATOR_NAME)
    public KeyGenerator sessionMethodKeyGenerator() {
        return new SessionMethodKeyGenerator();
    }

}
