package com.github.aqiu202.limit.config;


import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.limit.anno.CurrentLimiting;
import com.github.aqiu202.limit.anno.RepeatLimiting;
import com.github.aqiu202.limit.anno.ThreadLimiting;
import com.github.aqiu202.limit.aop.CurrentMethodInterceptor;
import com.github.aqiu202.limit.aop.RepeatMethodInterceptor;
import com.github.aqiu202.limit.aop.ThreadMethodInterceptor;
import com.github.aqiu202.lock.cache.CacheKeyLock;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ProxyLimitingConfiguration {

    public static final String REPEAT_LIMITING_BEAN_NAME = "repeatLimitingBean";
    public static final String THREAD_LIMITING_BEAN_NAME = "threadLimitingBean";
    public static final String CURRENT_LIMITING_BEAN_NAME = "currentLimitingBean";

    @Bean
    public RepeatMethodInterceptor repeatMethodInterceptor(CacheKeyLock cacheLock,
                                                           @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new RepeatMethodInterceptor(cacheLock, evaluationFiller);
    }

    @Bean(REPEAT_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = REPEAT_LIMITING_BEAN_NAME)
    public Advisor repeatLimitingBean(RepeatMethodInterceptor repeatMethodInterceptor) {
        return new AnnotationPointcutAdvisor<>(RepeatLimiting.class, repeatMethodInterceptor);
    }

    @Bean
    public ThreadMethodInterceptor threadMethodInterceptor(
            @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new ThreadMethodInterceptor(evaluationFiller);
    }

    @Bean(THREAD_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = THREAD_LIMITING_BEAN_NAME)
    public Advisor threadLimitingBean(ThreadMethodInterceptor threadMethodInterceptor) {
        return new AnnotationPointcutAdvisor<>(ThreadLimiting.class, threadMethodInterceptor);
    }

    @Bean
    public CurrentMethodInterceptor currentMethodInterceptor(
            @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new CurrentMethodInterceptor(evaluationFiller);
    }

    @Bean(CURRENT_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = CURRENT_LIMITING_BEAN_NAME)
    public Advisor currentLimitingBean(CurrentMethodInterceptor currentMethodInterceptor) {
        return new AnnotationPointcutAdvisor<>(CurrentLimiting.class, currentMethodInterceptor);
    }

}
