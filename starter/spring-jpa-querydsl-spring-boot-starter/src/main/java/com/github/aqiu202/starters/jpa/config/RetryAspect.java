package com.github.aqiu202.starters.jpa.config;

import com.github.aqiu202.starters.jpa.anno.Retry;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.orm.ObjectRetrievalFailureException;

@Aspect
@ConditionalOnMissingBean(value = RetryAspect.class)
public class RetryAspect {

    @Around("@within(com.github.aqiu202.starters.jpa.anno.Retry) || @within(com.github.aqiu202.starters.jpa.anno.OptimisticLockRetry)")
    public Object processForClass(ProceedingJoinPoint pjp) throws Throwable {
        Method method = this.getMethod(pjp);
        Retry mr = AnnotatedElementUtils.findMergedAnnotation(method, Retry.class);
        //如果方法上有相同注解，跳过类拦截
        if (mr != null) {
            return pjp.proceed();
        }
        Retry retry = AnnotatedElementUtils
                .findMergedAnnotation(pjp.getTarget().getClass(), Retry.class);
        return this.method(pjp, retry);
    }

    @Around("@annotation(com.github.aqiu202.starters.jpa.anno.Retry) || @annotation(com.github.aqiu202.starters.jpa.anno.OptimisticLockRetry)")
    // 指定拦截器规则
    public Object processForMethod(ProceedingJoinPoint pjp)
            throws Throwable {
        return this.method(pjp, null);
    }

    private Object method(ProceedingJoinPoint pjp, Retry retry)
            throws Throwable {
        if (retry == null) {
            retry = AnnotatedElementUtils.findMergedAnnotation(this.getMethod(pjp), Retry.class);
        }
        Class<? extends Throwable>[] value = retry.value();
        Class<? extends Throwable>[] exclude = retry.exclude();
        int time = retry.times();
        Retry canno = AnnotatedElementUtils
                .findMergedAnnotation(pjp.getTarget().getClass(), Retry.class);
        //如果类上有相同注解 合并注解参数 方法参数优先级更高
        if (canno != null) {
            if (value.length == 0) {
                value = canno.value();
            }
            if (exclude.length == 0) {
                exclude = canno.exclude();
            }
        }
        int attemptsNum = 0;
        do {
            attemptsNum++;
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                if (value.length > 0) {
                    for (Class<? extends Throwable> t : value) {
                        if (!t.isAssignableFrom(e.getClass())) {
                            throw e;
                        }
                    }
                }
                if (exclude.length > 0) {
                    for (Class<? extends Throwable> t : exclude) {
                        if (t.isAssignableFrom(e.getClass())) {
                            throw e;
                        }
                    }
                }
                if (attemptsNum > time) { // 如果大于重试次数，抛出ObjectRetrievalFailureException
                    throw new ObjectRetrievalFailureException("乐观锁失败次数过多！", e);
                }
            }
        } while (true);
    }

    /**
     * 根据aop切面获取当前执行的方法
     * @author AQIU 2018/8/8 上午11:16
     * @param pjp pjp
     * @return java.lang.reflect.Method
     **/
    private Method getMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }
}