package com.github.aqiu202.qlock.config;

import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.aop.spel.EvaluationFiller;
import com.github.aqiu202.id.config.IdGeneratorAutoConfiguration;
import com.github.aqiu202.lock.base.Lock;
import com.github.aqiu202.qlock.anno.QLock;
import com.github.aqiu202.qlock.aop.QLockMethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>QLockAutoConfiguration</pre>
 *
 * @author aqiu 2020/12/8 11:59
 **/
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({IdGeneratorAutoConfiguration.class})
public class QLockAutoConfiguration {

//    public static final String SIMPLE_ID_GENERATOR_FACTORY_BEAN_NAME = "simpleIdGeneratorFactoryBean";
//
//    @Bean(name = SIMPLE_ID_GENERATOR_FACTORY_BEAN_NAME)
//    @ConditionalOnMissingBean(name = SIMPLE_ID_GENERATOR_FACTORY_BEAN_NAME)
//    public IdGeneratorFactory simpleIdGeneratorFactoryBean(IdProperties idProperties) {
//        return new SimpleIdGeneratorFactory(idProperties.getType());
//    }

    @Bean
    public Advisor qLockInterceptorBean(
            @Autowired(required = false) EvaluationFiller evaluationFiller,
            Lock lock) {
        return new AnnotationPointcutAdvisor<>(QLock.class,
                new QLockMethodInterceptor(lock, evaluationFiller));
    }
}
