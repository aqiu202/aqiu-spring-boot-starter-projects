package com.github.aqiu202.limit.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.limit.anno.RepeatLimiting;
import com.github.aqiu202.lock.base.KeyLock;
import com.github.aqiu202.lock.base.ResourceLockedException;
import com.github.aqiu202.util.spel.EvaluationFiller;
import java.util.concurrent.TimeUnit;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>防重复提交-方法拦截</pre>
 *
 * @author aqiu 2020/11/30 0:01
 **/
public class RepeatMethodInterceptor extends AbstractKeyAnnotationInterceptor<RepeatLimiting> {

    private final KeyLock keyLock;

    public RepeatMethodInterceptor(KeyLock keyLock) {
        this.keyLock = keyLock;
    }

    public RepeatMethodInterceptor(KeyLock keyLock, EvaluationFiller evaluationFiller) {
        super(evaluationFiller);
        this.keyLock = keyLock;
    }

    @Override
    public String getKey(RepeatLimiting annotation) {
        return annotation.key();
    }

    @Override
    public String getKeyGeneratorName(RepeatLimiting annotation) {
        return annotation.keyGenerator();
    }

    @Override
    protected void beforeIntercept(MethodInvocation invocation, RepeatLimiting repeatLimiting,
            String key) {
        long timeout = repeatLimiting.timeout();
        TimeUnit timeUnit = repeatLimiting.timeUnit();
        final Boolean getLock = this.keyLock.acquire(key, timeout, timeUnit);
        if (!getLock) {
            throw new ResourceLockedException(repeatLimiting.message());
        }
    }

    @Override
    protected Object onError(MethodInvocation invocation, RepeatLimiting repeatLimiting, String key,
            Throwable throwable) throws Throwable {
        //只有等到过期或者出现异常才释放锁，不会主动释放锁
        this.keyLock.release(key);
        throw throwable;
    }
}
