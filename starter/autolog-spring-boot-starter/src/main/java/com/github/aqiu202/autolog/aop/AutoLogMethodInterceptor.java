package com.github.aqiu202.autolog.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.autolog.anno.AutoLog;
import com.github.aqiu202.autolog.config.AutoLogConfigurationBean;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.autolog.interceptor.ParamReader;
import com.github.aqiu202.autolog.result.LogRequestParam;
import com.github.aqiu202.autolog.util.AutoLogUtils;
import com.github.aqiu202.util.ServletRequestUtils;
import com.github.aqiu202.util.StringUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <pre>AutoLog日志拦截处理</pre>
 *
 * @author aqiu 2020/12/8 1:56
 **/
public class AutoLogMethodInterceptor extends AbstractKeyAnnotationInterceptor<AutoLog> implements LogMethodParamProcessor {

    private static final Logger log = LoggerFactory.getLogger(AutoLogMethodInterceptor.class);

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ParamReader paramReader;
    private final LogHandler logHandler;
    private final LogCollector logCollector;

    public AutoLogMethodInterceptor(AutoLogConfigurationBean cb) {
        super(cb.getEvaluationFiller());
        this.paramReader = cb.getParamReader();
        this.logCollector = cb.getLogCollector();
        this.logHandler = cb.getLogHandler();
    }

    @Override
    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    @Override
    public ParamReader getParamReader() {
        return paramReader;
    }

    @Override
    public String getKeyGeneratorName(AutoLog annotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getKey(AutoLog annotation) {
        return annotation.value();
    }

    @Override
    public Object intercept(MethodInvocation invocation, AutoLog autoLog, String key)
            throws Throwable {
        Object result;
        Throwable throwable = null;
        HttpServletRequest request = ServletRequestUtils.getCurrentRequest();
        if (request == null) {
            throw new IllegalArgumentException("请求异常");
        }
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            LogRequestParam param;
            //无自定义日志采集器
            if (this.logCollector == null) {
                param = AutoLogUtils.collect(request, this.generatorKey(invocation, autoLog));
                if (throwable == null) {
                    param.setResult(AutoLogUtils.LOG_RESULT_SUCCESS);
                } else {
                    param.setResult(AutoLogUtils.LOG_RESULT_FAil);
                }
            } else {
                //使用自定义的日志采集器
                param = this.logCollector
                        .collect(request, autoLog, throwable, invocation.getArguments());
                //日志信息未填充，会使用默认日志采集器填充
                if (StringUtils.isEmpty(param.getDesc())) {
                    param.setDesc(this.generatorKey(invocation, autoLog));
                }
                AutoLogUtils.handleDefaultValues(request, param, throwable);
            }
            log.info("result={}", param.getResult() == AutoLogUtils.LOG_RESULT_SUCCESS ? "成功" : "失败");
            if (this.logHandler != null) {
                this.logHandler.handle(param);
            }
        }
        return result;
    }

    @Override
    protected String generatorKey(MethodInvocation invocation, AutoLog annotation) {
        String template = this.getKey(annotation);
        Object[] args = invocation.getArguments();
        Method method = invocation.getMethod();
        if (StringUtils.isEmpty(template)) {
            template = this.handleParams(method, args);
        } else {
            template = this.processKey(template, invocation.getThis(), method, args);
        }
        return template;
    }

}
