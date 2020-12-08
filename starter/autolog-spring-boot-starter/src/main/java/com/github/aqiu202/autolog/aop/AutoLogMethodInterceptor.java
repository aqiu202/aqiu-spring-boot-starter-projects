package com.github.aqiu202.autolog.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.aop.util.ServletRequestUtils;
import com.github.aqiu202.autolog.anno.AutoLog;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.autolog.interceptor.ParamReader;
import com.github.aqiu202.autolog.result.LogRequestParam;
import com.github.aqiu202.autolog.result.LogRequestParam.DefaultLogRequestParam;
import com.github.aqiu202.autolog.util.CommonUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <pre>AutoLog日志拦截处理</pre>
 *
 * @author aqiu 2020/12/8 1:56
 **/
public class AutoLogMethodInterceptor extends AbstractKeyAnnotationInterceptor<AutoLog> {

    private static final Logger log = LoggerFactory.getLogger(AutoLogMethodInterceptor.class);

    public static final boolean LOG_RESULT_SUCCESS = true;
    public static final boolean LOG_RESULT_FAil = false;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ParamReader paramReader;
    private final LogHandler logHandler;
    private final LogCollector logCollector;

    public AutoLogMethodInterceptor(@NonNull ParamReader paramReader,
            @Nullable LogCollector logCollector,
            @Nullable LogHandler logHandler) {
        this.paramReader = paramReader;
        this.logCollector = logCollector;
        this.logHandler = logHandler;
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
                param = this.collect(request, this.generatorKey(invocation, autoLog));
                if (throwable == null) {
                    param.setResult(LOG_RESULT_SUCCESS);
                } else {
                    param.setResult(LOG_RESULT_FAil);
                }
            } else {
                //使用自定义的日志采集器
                param = this.logCollector
                        .collect(request, autoLog, throwable, invocation.getArguments());
                //日志信息未填充，会使用默认日志采集器填充
                if (StringUtils.isEmpty(param.getDesc())) {
                    param.setDesc(this.generatorKey(invocation, autoLog));
                }
                if (StringUtils.isEmpty(param.getIp())) {
                    param.setIp(CommonUtils.getIpAddress(request));
                }
                if (StringUtils.isEmpty(param.getUri())) {
                    param.setUri(request.getRequestURI().replace(request.getContextPath(), ""));
                }
                if (StringUtils.isEmpty(param.getMethod())) {
                    param.setMethod(request.getMethod());
                }
                if (param.getResult() == null) {
                    if (throwable == null) {
                        param.setResult(LOG_RESULT_SUCCESS);
                    } else {
                        param.setResult(LOG_RESULT_FAil);
                    }
                }
            }
            log.info("result={}", param.getResult() == LOG_RESULT_SUCCESS ? "成功" : "失败");
            if (this.logHandler != null) {
                this.logHandler.handle(param);
            }
        }
        return result;
    }

    private LogRequestParam collect(HttpServletRequest request, String desc) {
        LogRequestParam param = new DefaultLogRequestParam();
        String context = request.getContextPath();
        String uri = request.getRequestURI();
        // url
        uri = uri.replace(context, "");
        // method
        String method = request.getMethod();
        // ip
        String ip = CommonUtils.getIpAddress(request);
        log.info("url={}", uri);
        log.info("method={}", method);
        log.info("ip={}", ip);
        log.info("desc={}", desc);
        param.setIp(ip);
        param.setMethod(method);
        param.setUri(uri);
        param.setDesc(desc);
        return param;
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
        return CommonUtils.stringFormat(joiner.toString(), paramMap).replace("[", "{")
                .replace("]", "}");
    }

}
