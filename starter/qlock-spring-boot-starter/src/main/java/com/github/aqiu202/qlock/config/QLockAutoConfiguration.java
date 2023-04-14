package com.github.aqiu202.qlock.config;

import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.lock.base.DefaultLockCodeExecutor;
import com.github.aqiu202.lock.base.Lock;
import com.github.aqiu202.lock.base.LockCodeExecutor;
import com.github.aqiu202.qlock.anno.QLock;
import com.github.aqiu202.qlock.aop.QLockMethodInterceptor;
import com.github.aqiu202.qlock.aop.QLockRequestListener;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequestListener;

/**
 * <pre>QLockAutoConfiguration</pre>
 *
 * @author aqiu 2020/12/8 11:59
 **/
@Configuration(proxyBeanMethods = false)
public class QLockAutoConfiguration {

    @Bean
    public Advisor qLockInterceptorBean(
            @Autowired(required = false) EvaluationFiller evaluationFiller, Lock lock) {
        return new AnnotationPointcutAdvisor<>(QLock.class,
                new QLockMethodInterceptor(lock, evaluationFiller));
    }

    @Bean
    @ConditionalOnMissingBean(value = LockCodeExecutor.class)
    public LockCodeExecutor lockCodeExecutor(Lock lock) {
        return new DefaultLockCodeExecutor(lock);
    }

    @Bean
    public ServletRequestListener qLockRequestListener() {
        return new QLockRequestListener();
    }
}
