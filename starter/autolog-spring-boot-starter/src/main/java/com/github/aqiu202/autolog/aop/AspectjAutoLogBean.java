package com.github.aqiu202.autolog.aop;


import com.github.aqiu202.aop.pointcut.SPelKeyHandler;
import com.github.aqiu202.autolog.anno.AutoLog;
import com.github.aqiu202.autolog.config.AutoLogConfigurationBean;
import com.github.aqiu202.autolog.interceptor.LogCollector;
import com.github.aqiu202.autolog.interceptor.LogHandler;
import com.github.aqiu202.autolog.interceptor.ParamReader;
import com.github.aqiu202.autolog.result.LogRequestParam;
import com.github.aqiu202.autolog.util.AutoLogUtils;
import com.github.aqiu202.util.ServletRequestUtils;
import com.github.aqiu202.util.StringUtils;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
 * AutoLog日志aop切面类
 *
 * @author aqiu 2018年10月24日 下午3:54:16
 */
@Aspect
public class AspectjAutoLogBean implements SPelKeyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectjAutoLogBean.class);


    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ParamReader paramReader;

    public AspectjAutoLogBean(AutoLogConfigurationBean cb) {
        this.paramReader = cb.getParamReader();
        this.logCollector = cb.getLogCollector();
        this.logHandler = cb.getLogHandler();
        this.evaluationFiller = cb.getEvaluationFiller();
    }

    private final LogHandler logHandler;
    private final LogCollector logCollector;
    private final EvaluationFiller evaluationFiller;

    /**
     * 类拦截
     *
     * @param pjp: 切点
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:21 上午
     **/
    @Around("@within(com.github.aqiu202.autolog.anno.AutoLog)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Method m = this.getMethod(pjp);
        AutoLog manno = m.getDeclaredAnnotation(AutoLog.class);
        //方法上有相同注解 跳过类拦截
        if (manno != null) {
            return pjp.proceed();
        }
        Object t = pjp.getTarget();
        Class<?> c = t.getClass();
        AutoLog anno = c.getDeclaredAnnotation(AutoLog.class);
        return this.interceptor(pjp, anno);
    }

    /**
     * 方法拦截
     *
     * @param pjp:     切点
     * @param autoLog: 注解信息
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:21 上午
     **/
    @Around("@annotation(autoLog)")
    public Object interceptor(ProceedingJoinPoint pjp, AutoLog autoLog) throws Throwable {
        Object[] objs = pjp.getArgs();
        Object result;
        Throwable throwable = null;
        HttpServletRequest request = ServletRequestUtils.getCurrentRequest();
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
                param = AutoLogUtils.collect(request, this.genDesc(pjp, autoLog));
                if (throwable == null) {
                    param.setResult(AutoLogUtils.LOG_RESULT_SUCCESS);
                } else {
                    param.setResult(AutoLogUtils.LOG_RESULT_FAil);
                }
            } else {
                //使用自定义的日志采集器
                param = this.logCollector.collect(request, autoLog, throwable, objs);
                //日志信息未填充，会使用默认日志采集器填充
                if (StringUtils.isEmpty(param.getDesc())) {
                    param.setDesc(this.genDesc(pjp, autoLog));
                }
                AutoLogUtils.handleDefaultValues(request, param, throwable);
            }
            LOGGER.info("result={}", param.getResult() == AutoLogUtils.LOG_RESULT_SUCCESS ? "成功" : "失败");
            if (this.logHandler != null) {
                this.logHandler.handle(param);
            }
        }
        return result;
    }

    /**
     * 获取描述信息
     *
     * @param pjp     切点
     * @param autoLog 注解信息
     * @return {@link String}
     */
    private String genDesc(ProceedingJoinPoint pjp, AutoLog autoLog) {
        String template = autoLog.value();
        Object[] args = pjp.getArgs();
        Method method = this.getMethod(pjp);
        if (StringUtils.isEmpty(template)) {
            template = this.handleParams(method, args);
        } else {
            template = this.processKey(template, pjp.getThis(), method, args, this.evaluationFiller);
        }
        return template;
    }

    /**
     * 根据aop切面获取当前执行的方法
     *
     * @param pjp 切点
     * @return {@link Method}
     * @author AQIU 2018/8/8 上午11:16
     **/
    private Method getMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }


    /**
     * 根据方法参数生成描述
     *
     * @param args 参数
     * @return {@link String}
     * @author AQIU 2018/8/8 上午11:31
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
            paramMap.put(String.valueOf(i), paramReader.apply(o));
        }
        return StringUtils.stringFormat(joiner.toString(), paramMap).replace("[", "{")
                .replace("]", "}");
    }

}
