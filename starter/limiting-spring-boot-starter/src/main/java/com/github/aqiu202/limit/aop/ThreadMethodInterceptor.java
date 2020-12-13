package com.github.aqiu202.limit.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.util.spel.EvaluationFiller;
import com.github.aqiu202.limit.anno.ThreadLimiting;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>并发线程数限流</pre>
 *
 * @author aqiu 2020/11/30 0:01
 **/
public class ThreadMethodInterceptor extends AbstractKeyAnnotationInterceptor<ThreadLimiting> {


    private final Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    public ThreadMethodInterceptor() {
    }

    public ThreadMethodInterceptor(EvaluationFiller evaluationFiller) {
        super(evaluationFiller);
    }

    @Override
    public String getKey(ThreadLimiting annotation) {
        return annotation.key();
    }

    @Override
    public String getKeyGeneratorName(ThreadLimiting annotation) {
        return annotation.keyGenerator();
    }

    @Override
    protected void beforeIntercept(MethodInvocation invocation, ThreadLimiting threadLimiting, String key) {
        Semaphore semaphore;
        if ((semaphore = semaphoreMap.get(key)) == null) {
            semaphore = new Semaphore(threadLimiting.threads());
            semaphoreMap.put(key, semaphore);
        }
        if (!semaphore.tryAcquire()) {
            throw new IllegalArgumentException(threadLimiting.message());
        }
    }

    @Override
    protected void afterIntercept(MethodInvocation invocation, ThreadLimiting threadLimiting, String key, Throwable throwable) {
        Semaphore semaphore = semaphoreMap.get(key);
        if (semaphore != null) {
            semaphore.release();
        }
    }
}
