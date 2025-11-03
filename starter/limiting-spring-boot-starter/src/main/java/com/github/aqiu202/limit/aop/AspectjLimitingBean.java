package com.github.aqiu202.limit.aop;


import com.github.aqiu202.aop.keygen.KeyGenerator;
import com.github.aqiu202.aop.keygen.impl.MethodKeyGenerator;
import com.github.aqiu202.aop.pointcut.SPelKeyHandler;
import com.github.aqiu202.limit.anno.CurrentLimiting;
import com.github.aqiu202.limit.anno.RepeatLimiting;
import com.github.aqiu202.limit.anno.ThreadLimiting;
import com.github.aqiu202.limit.key.SessionMethodKeyGenerator;
import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.util.ServletRequestUtils;
import com.github.aqiu202.util.StringUtils;
import com.github.aqiu202.util.spel.EvaluationFiller;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Aspect
public class AspectjLimitingBean implements SPelKeyHandler, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final KeyGenerator defaultKeyGenerator = new SessionMethodKeyGenerator();
    private final MethodKeyGenerator methodKeyGenerator = new MethodKeyGenerator();
    private final Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
    private final KeyLock keyLock;
    private final EvaluationFiller evaluationFiller;

    private final ExpressionParser parser = new SpelExpressionParser();
    private ApplicationContext applicationContext;

    public AspectjLimitingBean(KeyLock keyLock, EvaluationFiller evaluationFiller) {
        this.keyLock = keyLock;
        this.evaluationFiller = evaluationFiller;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 防重复提交（限时锁）-类拦截
     *
     * @param pjp: 切点
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:19 上午
     **/
    @Around("@within(com.github.aqiu202.limit.anno.RepeatLimiting)")
    public Object repeats(ProceedingJoinPoint pjp) throws Throwable {
        Method m = this.getMethod(pjp);
        RepeatLimiting manno = m.getDeclaredAnnotation(RepeatLimiting.class);
        //方法上有相同注解 跳过类拦截
        if (manno != null) {
            return pjp.proceed();
        }
        Object t = pjp.getTarget();
        Class<?> c = t.getClass();
        RepeatLimiting anno = c.getDeclaredAnnotation(RepeatLimiting.class);
        return this.repeat(pjp, anno);
    }

    /**
     * 防重复提交（限时锁）-方法拦截
     *
     * @param pjp:            切点
     * @param repeatLimiting: 注解信息
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:18 上午
     **/
    @Around("@annotation(repeatLimiting)")
    public Object repeat(ProceedingJoinPoint pjp, RepeatLimiting repeatLimiting)
            throws Throwable {
        String key = repeatLimiting.key();
        String generatorName = repeatLimiting.keyGenerator();
        long timeout = repeatLimiting.timeout();
        TimeUnit timeUnit = repeatLimiting.timeUnit();
        //如果类上有相同注解 合并注解参数 方法参数优先级更高
        RepeatLimiting canno = pjp.getTarget().getClass()
                .getDeclaredAnnotation(RepeatLimiting.class);
        if (canno != null) {
            if (StringUtils.isEmpty(generatorName)) {
                generatorName = canno.keyGenerator();
            }
            if (timeout <= 0) {
                timeout = canno.timeout();
                timeUnit = canno.timeUnit();
            }
            if (StringUtils.isEmpty(key)) {
                key = canno.key();
            }
        }
        Method m = this.getMethod(pjp);
        Object[] params = pjp.getArgs();
        Object target = pjp.getTarget();
        if (StringUtils.isEmpty(key)) {
            key = this.generatorKey(generatorName, target, m, params);
        } else {
            key = this.processKey(key, target, m, params, this.evaluationFiller);
        }
        Boolean unlocked = keyLock.acquire(key, timeout, timeUnit);
        if (!unlocked) {
            throw new IllegalArgumentException(repeatLimiting.message());
        }
        Throwable error = null;
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            logger.error("", throwable);
            error = throwable;
            throw throwable;
        } finally {
            if (error != null) {
                keyLock.release(key);
            }
        }
        return result;
    }

    /**
     * 并发线程数限流-类拦截
     *
     * @param pjp: 切点
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:15 上午
     **/
    @Around("@within(com.github.aqiu202.limit.anno.ThreadLimiting)")
    public Object threads(ProceedingJoinPoint pjp) throws Throwable {
        Method m = this.getMethod(pjp);
        ThreadLimiting manno = m.getDeclaredAnnotation(ThreadLimiting.class);
        //方法上有相同注解 跳过类拦截
        if (manno != null) {
            return pjp.proceed();
        }
        Object t = pjp.getTarget();
        Class<?> c = t.getClass();
        ThreadLimiting anno = c.getDeclaredAnnotation(ThreadLimiting.class);
        return this.thread(pjp, anno);
    }

    /**
     * 并发线程数限流-方法拦截
     *
     * @param pjp:            切点
     * @param threadLimiting: 注解信息
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:15 上午
     **/
    @Around("@annotation(threadLimiting)")
    public Object thread(ProceedingJoinPoint pjp, ThreadLimiting threadLimiting)
            throws Throwable {
        String generatorName = threadLimiting.keyGenerator();
        String key = threadLimiting.key();
        ThreadLimiting canno = pjp.getTarget().getClass()
                .getDeclaredAnnotation(ThreadLimiting.class);
        //如果类上有相同注解 合并注解参数 方法参数优先级更高
        if (canno != null) {
            if (StringUtils.isEmpty(generatorName)) {
                generatorName = canno.keyGenerator();
            }
            if (StringUtils.isEmpty(key)) {
                key = canno.key();
            }
        }
        Method m = this.getMethod(pjp);
        Object[] params = pjp.getArgs();
        Object target = pjp.getTarget();
        if (StringUtils.isEmpty(key)) {
            key = this.generatorKeyByMethod(generatorName, target, m, params);
        } else {
            key = this.processKey(key, target, m, params, this.evaluationFiller);
        }
        Semaphore semaphore;
        if ((semaphore = semaphoreMap.get(key)) == null) {
            semaphore = new Semaphore(threadLimiting.threads(), true);
            semaphoreMap.put(key, semaphore);
        }
        if (!semaphore.tryAcquire()) {
            throw new IllegalArgumentException(threadLimiting.message());
        }
        Object result;
        try {
            result = pjp.proceed();
        } finally {
            semaphore.release();
        }
        return result;
    }

    /**
     * 令牌桶算法限流-类拦截
     *
     * @param pjp: 切点
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:16 上午
     **/
    @Around("@within(com.github.aqiu202.limit.anno.CurrentLimiting)")
    public Object currents(ProceedingJoinPoint pjp) throws Throwable {
        Method m = this.getMethod(pjp);
        CurrentLimiting manno = m.getDeclaredAnnotation(CurrentLimiting.class);
        //方法上有相同注解 跳过类拦截
        if (manno != null) {
            return pjp.proceed();
        }
        Object t = pjp.getTarget();
        Class<?> c = t.getClass();
        CurrentLimiting anno = c.getDeclaredAnnotation(CurrentLimiting.class);
        return this.current(pjp, anno);
    }

    /**
     * 令牌桶算法限流-方法拦截
     *
     * @param pjp:             切点
     * @param currentLimiting: 注解信息
     * @return {@link Object}
     * @throws Throwable 任意异常
     * @author aqiu 2020/2/8 1:20 上午
     **/
    @Around("@annotation(currentLimiting)")
    public Object current(ProceedingJoinPoint pjp, CurrentLimiting currentLimiting)
            throws Throwable {
        String generatorName = currentLimiting.keyGenerator();
        String key = currentLimiting.key();
        CurrentLimiting canno = pjp.getTarget().getClass()
                .getDeclaredAnnotation(CurrentLimiting.class);
        //如果类上有相同注解 合并注解参数 方法参数优先级更高
        if (canno != null) {
            if (StringUtils.isEmpty(generatorName)) {
                generatorName = canno.keyGenerator();
            }
            if (StringUtils.isEmpty(key)) {
                key = canno.key();
            }
        }
        Method m = this.getMethod(pjp);
        Object[] params = pjp.getArgs();
        Object target = pjp.getTarget();
        if (StringUtils.isEmpty(key)) {
            key = this.generatorKeyByMethod(generatorName, target, m, params);
        } else {
            key = this.processKey(key, target, m, params, this.evaluationFiller);
        }
        RateLimiter limiter;
        if ((limiter = rateLimiterMap.get(key)) == null) {
            limiter = RateLimiter.create(currentLimiting.permits());
            rateLimiterMap.put(key, limiter);
        }
        if (!limiter.tryAcquire()) {
            throw new IllegalArgumentException(currentLimiting.message());
        }
        return pjp.proceed();
    }

    private String generatorKey(String keyGeneratorName, Object target, Method m,
                                Object[] params) {
        HttpServletRequest request = ServletRequestUtils.getCurrentRequest();
        String key;
        if (StringUtils.isEmpty(keyGeneratorName)) {
            key = this.defaultKeyGenerator.generate(target, m, params);
        } else {
            KeyGenerator keyGenerator;
            try {
                keyGenerator = this.applicationContext
                        .getBean(keyGeneratorName, KeyGenerator.class);
            } catch (Exception e) {
                logger.error("KeyGenerator 配置错误，没有找到名称为" + keyGeneratorName + "的KeyGenerator：",
                        e);
                keyGenerator = this.defaultKeyGenerator;
            }
            key = keyGenerator.generate(target, m, params);
        }
        return key;
    }

    private String generatorKeyByMethod(String keyGeneratorName, Object target, Method m,
                                        Object[] params) {
        String key;
        if (StringUtils.isEmpty(keyGeneratorName)) {
            key = this.methodKeyGenerator.generate(target, m, params);
        } else {
            MethodKeyGenerator keyGenerator;
            try {
                keyGenerator = this.applicationContext
                        .getBean(keyGeneratorName, MethodKeyGenerator.class);
            } catch (Exception e) {
                logger.error("MethodKeyGenerator 配置错误，没有找到名称为" + keyGeneratorName
                                + "的MethodKeyGenerator：",
                        e);
                keyGenerator = this.methodKeyGenerator;
            }
            key = keyGenerator.generate(target, m, params);
        }
        return key;
    }

    /**
     * 根据aop切面获取当前执行的方法
     *
     * @param pjp pjp
     * @return {@link Method}
     * @throws Throwable 任意异常
     * @author AQIU 2018/8/8 上午11:16
     **/
    private Method getMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

}
