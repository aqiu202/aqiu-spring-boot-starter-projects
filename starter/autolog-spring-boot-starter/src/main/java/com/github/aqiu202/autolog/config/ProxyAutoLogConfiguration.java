package com.github.aqiu202.autolog.config;

import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.autolog.anno.AutoLog;
import com.github.aqiu202.autolog.aop.AutoLogMethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>AutoLog配置类</pre>
 *
 * @author aqiu 2018年10月24日 下午3:55:02
 */
@Configuration(proxyBeanMethods = false)
public class ProxyAutoLogConfiguration {

    private static final String AOP_LOGGER_BEAN_NAME = "aopLoggerBean";

    @Bean(name = AOP_LOGGER_BEAN_NAME)
    @ConditionalOnMissingBean(name = AOP_LOGGER_BEAN_NAME)
    public Advisor aopLoggerBean(AutoLogConfigurationBean cb) {
        return new AnnotationPointcutAdvisor<>(AutoLog.class,
                new AutoLogMethodInterceptor(cb));
    }

}
