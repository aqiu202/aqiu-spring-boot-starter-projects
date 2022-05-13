package com.github.aqiu202.limit.config;

import com.github.aqiu202.limit.aop.AspectjLimitingBean;
import com.github.aqiu202.lock.base.CacheLock;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AspectjLimitingConfiguration {

    public static final String ASPECTJ_LIMITING_BEAN_NAME = "aspectjLimitingBean";

    @Bean(name = ASPECTJ_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = ASPECTJ_LIMITING_BEAN_NAME)
    public AspectjLimitingBean repeatLimitingBean(CacheLock cacheLock,
                                                  @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new AspectjLimitingBean(cacheLock, evaluationFiller);
    }
}
