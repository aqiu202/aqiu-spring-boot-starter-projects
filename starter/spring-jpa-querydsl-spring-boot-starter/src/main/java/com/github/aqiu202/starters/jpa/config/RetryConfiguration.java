package com.github.aqiu202.starters.jpa.config;

import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.starters.jpa.anno.Retry;
import com.github.aqiu202.starters.jpa.aop.RetryMethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>{@link RetryConfiguration}</pre>
 *
 * @author aqiu 2020/12/13 20:26
 **/
@Configuration(proxyBeanMethods = false)
public class RetryConfiguration {

    public static final String RETRY_METHOD_INTERCEPTOR_BEAN_NAME = "retryMethodInterceptorBean";

    @Bean(name = RETRY_METHOD_INTERCEPTOR_BEAN_NAME)
    @ConditionalOnMissingBean(name = RETRY_METHOD_INTERCEPTOR_BEAN_NAME)
    public Advisor retryMethodInterceptor() {
        return new AnnotationPointcutAdvisor<>(Retry.class, new RetryMethodInterceptor());
    }
}
