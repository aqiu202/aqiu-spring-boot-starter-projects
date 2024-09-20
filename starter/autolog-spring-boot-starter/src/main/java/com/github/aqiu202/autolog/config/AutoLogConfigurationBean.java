package com.github.aqiu202.autolog.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.autolog.interceptor.ParamReader;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>AutoLog通用配置类</pre>
 *
 * @author aqiu 2018年10月24日 下午3:55:02
 */
@Configuration(proxyBeanMethods = false)
public class AutoLogConfigurationBean {

    private final ParamReader paramReader;
    private final LogHandler logHandler;
    private final LogCollector logCollector;
    private final EvaluationFiller evaluationFiller;

    public AutoLogConfigurationBean(ObjectMapper objectMapper,
                                    @Autowired(required = false) ParamReader paramReader,
                                    @Autowired(required = false) LogCollector logCollector,
                                    @Autowired(required = false) LogHandler logHandler,
                                    @Autowired(required = false) EvaluationFiller evaluationFiller) {
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
        this.paramReader = defaultParamReader;
        this.logCollector = logCollector;
        this.logHandler = logHandler;
        this.evaluationFiller = evaluationFiller;
    }

    public ParamReader getParamReader() {
        return paramReader;
    }

    public LogHandler getLogHandler() {
        return logHandler;
    }

    public LogCollector getLogCollector() {
        return logCollector;
    }

    public EvaluationFiller getEvaluationFiller() {
        return evaluationFiller;
    }
}
