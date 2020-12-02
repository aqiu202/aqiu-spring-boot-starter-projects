package com.github.aqiu202.qlock.aop;

import com.github.aqiu202.aop.pointcut.AbstractKeyAnnotationInterceptor;
import com.github.aqiu202.qlock.anno.QLock;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>QLockMethodInterceptor</pre>
 *
 * @author aqiu 2020/12/2 13:26
 **/
public class QLockMethodInterceptor extends AbstractKeyAnnotationInterceptor<QLock> {

    @Override
    public String getKeyGeneratorName(QLock annotation) {
        return annotation.keyGenerator();
    }

    @Override
    public String getKey(QLock annotation) {
        return annotation.key();
    }

    @Override
    public Object intercept(MethodInvocation invocation, QLock qLock, String key) throws Throwable {
        return null;
    }
}
