package com.github.aqiu202.qlock.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.lock.base.ResourceLockedException;
import com.github.aqiu202.util.spel.EvaluationFiller;
import com.github.aqiu202.qlock.anno.QLock;
import java.util.concurrent.TimeUnit;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>QLockMethodInterceptor</pre>
 *
 * @author aqiu 2020/12/2 13:26
 **/
public class QLockMethodInterceptor extends AbstractKeyAnnotationInterceptor<QLock> {

    private final KeyLock keyLock;

    public QLockMethodInterceptor(KeyLock keyLock) {
        this.keyLock = keyLock;
    }

    public QLockMethodInterceptor(KeyLock keyLock, EvaluationFiller evaluationFiller) {
        super(evaluationFiller);
        this.keyLock = keyLock;
    }

    @Override
    public String getKeyGeneratorName(QLock annotation) {
        return annotation.keyGenerator();
    }

    @Override
    public String getKey(QLock annotation) {
        return annotation.key();
    }

    @Override
    protected void beforeIntercept(MethodInvocation invocation, QLock qLock, String key) {
        long timeout = qLock.timeout();
        TimeUnit timeUnit = qLock.timeUnit();
        final Boolean getLock = this.keyLock.acquire(key, timeout, timeUnit);
        if (!getLock) {
            throw new ResourceLockedException(qLock.message());
        }
    }

    @Override
    protected void afterIntercept(MethodInvocation invocation, QLock qLock, String key, Throwable throwable) {
        this.keyLock.release(key, qLock.timeout(), qLock.timeUnit());
    }
}
