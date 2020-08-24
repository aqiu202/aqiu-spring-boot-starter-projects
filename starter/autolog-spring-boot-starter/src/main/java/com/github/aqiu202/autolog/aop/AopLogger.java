package com.github.aqiu202.autolog.aop;


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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AutoLog日志aop切面类
 * @author aqiu 2018年10月24日 下午3:54:16
 */
@Aspect
public class AopLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopLogger.class);

    public static final boolean LOG_RESULT_SUCCESS = true;
    public static final boolean LOG_RESULT_FAil = false;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ParamReader paramReader;

    public AopLogger(@NonNull ParamReader paramReader,
            @Nullable LogCollector logCollector,
            @Nullable LogHandler logHandler) {
        this.paramReader = paramReader;
        this.logCollector = logCollector;
        this.logHandler = logHandler;
    }

    private final LogHandler logHandler;
    private final LogCollector logCollector;

    /**
     * <pre>类拦截</pre>
     * @author aqiu
     * @param pjp: 切点
     * @return {@link Object}
     * @throws Throwable 异常信息
     **/
    @Around("@within(com.github.aqiu202.autolog.anno.AutoLog)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Method m = this.getMethod(pjp);
        AutoLog manno = m.getDeclaredAnnotation(AutoLog.class);
        //方法上有相同注解 跳过类拦截，只使用方法切入拦截
        if (manno != null) {
            return pjp.proceed();
        }
        Object t = pjp.getTarget();
        Class<?> c = t.getClass();
        AutoLog anno = c.getDeclaredAnnotation(AutoLog.class);
        return this.interceptor(pjp, anno);
    }

    /**
     * <pre>方法拦截（类上有@AutoLog注解或者方法上有注解时都进入此方法）</pre>
     * @author aqiu
     * @param pjp: 切点
     * @param autoLog: 注解信息
     * @return {@link Object}
     * @throws Throwable 异常信息
     **/
    @Around("@annotation(autoLog)")
    public Object interceptor(ProceedingJoinPoint pjp, AutoLog autoLog) throws Throwable {
        Object[] objs = pjp.getArgs();
        Object result;
        Throwable throwable = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request =
                servletRequestAttributes == null ? null : servletRequestAttributes.getRequest();
        if (request == null) {
            return pjp.proceed();
        }
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            LogRequestParam param;
            //无自定义日志采集器
            if (this.logCollector == null) {
                param = this.collect(request, this.genDesc(pjp, autoLog));
                if (throwable == null) {
                    param.setResult(LOG_RESULT_SUCCESS);
                } else {
                    param.setResult(LOG_RESULT_FAil);
                }
            } else {
                //使用自定义的日志采集器
                param = this.logCollector.collect(request, autoLog, throwable, objs);
                //日志信息未填充，会使用默认日志采集器填充
                if (StringUtils.isEmpty(param.getDesc())) {
                    param.setDesc(this.genDesc(pjp, autoLog));
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
            LOGGER.info("result={}", param.getResult() == LOG_RESULT_SUCCESS ? "成功" : "失败");
            if (this.logHandler != null) {
                this.logHandler.handle(param);
            }
        }
        return result;
    }

    /**
     * <pre>获取描述信息</pre>
     * @param pjp 切点
     * @param autoLog 注解信息
     * @return 解析后的日志信息
     */
    private String genDesc(ProceedingJoinPoint pjp, AutoLog autoLog) {
        String template = autoLog.value();
        AutoLog canno = pjp.getTarget().getClass()
                .getDeclaredAnnotation(AutoLog.class);
        //类和方法上都有@AutoLog注解时，方法注解没有参数默认使用类注解覆盖
        if (canno != null) {
            if (StringUtils.isEmpty(template)) {
                template = canno.value();
            }
        }
        Object[] objs = pjp.getArgs();
        Method targetMethod = this.getMethod(pjp);
        if (StringUtils.isEmpty(template)) {
            template = this.handleParams(targetMethod, objs);
        } else {
            template = this.handleParams(targetMethod, objs, template);
        }
        return template;
    }

    /**
     * <pre>根据aop切面获取当前执行的方法</pre>
     * @author aqiu
     * @param pjp 切点
     * @return 切点当前方法
     **/
    private Method getMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    /**
     * <pre>模板和参数生成描述</pre>
     * @author aqiu
     * @param method 切点方法
     * @param args 方法参数
     * @param template 日志描述模板
     * @return 日志描述
     **/
    private String handleParams(Method method, Object[] args, String template) {
        return this.processTemplate(template, method, args);
    }

    /**
     * <pre>根据方法参数生成描述</pre>
     * @author aqiu
     * @param args 参数
     * @return 自动生成的日志描述信息
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
        LOGGER.info("url={}", uri);
        LOGGER.info("method={}", method);
        LOGGER.info("ip={}", ip);
        LOGGER.info("desc={}", desc);
        param.setIp(ip);
        param.setMethod(method);
        param.setUri(uri);
        param.setDesc(desc);
        return param;
    }

    private String processSpElTemplate(String template, Method method, Object[] parameters) {
        @SuppressWarnings("ConstantConditions") MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                null, method, parameters, this.parameterNameDiscoverer);
        return this.parser.parseExpression(template).getValue(context, String.class);
    }

    private String processTemplate(String template, Method method, Object[] parameters) {
        if (this.isSpEl(template)) {
            template = this.processSpElTemplate(template, method, parameters);
        } else {
            template = this.processNotSpElTemplate(template);
        }
        return template;
    }

    private boolean isSpEl(String key) {
        return key.replace("\\#", "").contains("#");
    }

    private String processNotSpElTemplate(String key) {
        return key.replace("\\#", "#");
    }


}
