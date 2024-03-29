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
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * <pre>AutoLog日志拦截处理</pre>
 *
 * @author aqiu 2020/12/8 1:56
 **/
public class AutoLogMethodInterceptor extends AbstractKeyAnnotationInterceptor<AutoLog> {

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

    /**
     * <pre>根据方法参数生成描述</pre>
     *
     * @param args 参数
     * @return 自动生成的日志描述信息
     * @author aqiu
     **/
    private String handleParams(Method method, Object[] args) {
        String[] paramNames = this.parameterNameDiscoverer.getParameterNames(method);
        Assert.notNull(paramNames, "获取方法参数名称失败");
        StringJoiner joiner = new StringJoiner(",", "请求参数：[", "]");
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            if (o instanceof ServletRequest || o instanceof ServletResponse) {
                continue;
            }
            String value = paramNames[i] + ":{" + i + "}";
            joiner.add(value);
            paramMap.put(String.valueOf(i), this.paramReader.apply(o));
        }
        return StringUtils.stringFormat(joiner.toString(), paramMap).replace("[", "{")
                .replace("]", "}");
    }

}
