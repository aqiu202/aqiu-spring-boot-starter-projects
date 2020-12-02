package com.github.aqiu202.limit.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.aop.spel.EvaluationFiller;
import com.github.aqiu202.limit.anno.CurrentLimiting;
import com.google.common.util.concurrent.RateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * <pre>令牌桶算法限流</pre>
 *
 * @author aqiu 2020/11/30 0:01
 **/
public class CurrentMethodInterceptor extends AbstractKeyAnnotationInterceptor<CurrentLimiting> {

    private static final Logger logger = LoggerFactory.getLogger(CurrentMethodInterceptor.class);

    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    public CurrentMethodInterceptor() {
    }

    public CurrentMethodInterceptor(EvaluationFiller evaluationFiller) {
        super(evaluationFiller);
    }

    @Override
    public Object intercept(MethodInvocation invocation, CurrentLimiting currentLimiting, String key)
            throws Throwable {
        RateLimiter limiter;
        if ((limiter = rateLimiterMap.get(key)) == null) {
            limiter = RateLimiter.create(currentLimiting.permits());
            rateLimiterMap.put(key, limiter);
        }
        if (!limiter.tryAcquire()) {
            throw new IllegalArgumentException(currentLimiting.message());
        }
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable t) {
            logger.error("", t);
            throw t;
        }
        return result;
    }

    @Override
    public String getKey(CurrentLimiting annotation) {
        return annotation.key();
    }

    @Override
    public String getKeyGeneratorName(CurrentLimiting annotation) {
        return annotation.keyGenerator();
    }
}
