package com.github.aqiu202.limit.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.aop.spel.EvaluationFiller;
import com.github.aqiu202.limit.anno.ThreadLimiting;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * <pre>并发线程数限流</pre>
 *
 * @author aqiu 2020/11/30 0:01
 **/
public class ThreadMethodInterceptor extends AbstractKeyAnnotationInterceptor<ThreadLimiting> {

    private static final Logger logger = LoggerFactory.getLogger(ThreadMethodInterceptor.class);

    private final Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    public ThreadMethodInterceptor() {
    }

    public ThreadMethodInterceptor(EvaluationFiller evaluationFiller) {
        super(evaluationFiller);
    }

    @Override
    public Object intercept(MethodInvocation invocation, ThreadLimiting threadLimiting, String key)
            throws Throwable {
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
            result = invocation.proceed();
        } catch (Throwable t) {
            logger.error("", t);
            throw t;
        } finally {
            semaphore.release();
        }
        return result;
    }

    @Override
    public String getKey(ThreadLimiting annotation) {
        return annotation.key();
    }

    @Override
    public String getKeyGeneratorName(ThreadLimiting annotation) {
        return annotation.keyGenerator();
    }
}
