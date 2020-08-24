package com.github.aqiu202.autolog.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aqiu202.autolog.aop.AopLogger;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.autolog.interceptor.ParamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>AutoLog配置类</pre>
 * @author aqiu 2018年10月24日 下午3:55:02
 */
@Configuration(proxyBeanMethods = false)
public class AutoLogConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AopLogger aopLogger(
            ObjectMapper objectMapper,
            @Autowired(required = false) ParamReader paramReader,
            @Autowired(required = false) LogCollector logCollector,
            @Autowired(required = false) LogHandler logHandler) {
        ParamReader defaultParamReader;
        if (paramReader == null) {
            defaultParamReader = (obj) -> {
                String s;
                try {
                    s = objectMapper.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                    return null;
                }
                return s;
            };
        } else {
            defaultParamReader = paramReader;
        }
        return new AopLogger(defaultParamReader, logCollector, logHandler);
    }

}
