package com.github.aqiu202.starters.jpa.aop;

import com.github.aqiu202.aop.pointcut.SimpleAnnotationInterceptor;
import com.github.aqiu202.starters.jpa.anno.Retry;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * <pre>{@link RetryMethodInterceptor}</pre>
 *
 * @author aqiu 2020/12/13 20:27
 **/
public class RetryMethodInterceptor extends SimpleAnnotationInterceptor<Retry> {

    @Override
    public Object onError(MethodInvocation invocation, Retry retry, Throwable throwable)
            throws Throwable {
        return this.handleError(invocation, retry, throwable, 1);
    }

    public Object handleError(MethodInvocation invocation, Retry retry, Throwable throwable,
            int currentTimes) throws Throwable {
        if (this.needRetry(retry, throwable)) {
            try {
                this.outTimes(retry, throwable, currentTimes);
                final int delay = retry.delay();
                if (delay > 0) {
                    Thread.sleep(delay);
                }
                return this.doIntercept(invocation, retry);
            } catch (Throwable e) {
                return this.handleError(invocation, retry, e, ++currentTimes);
            }
        } else {
            return super.onError(invocation, retry, throwable);
        }
    }

    private boolean needRetry(Retry retry, Throwable throwable) {
        //避免死循环
        if (throwable instanceof ObjectRetrievalFailureException) {
            return false;
        }
        Class<? extends Throwable>[] value = retry.value();
        Class<? extends Throwable>[] exclude = retry.exclude();
        if (value.length > 0) {
            return this.hasAssignableFrom(value, throwable.getClass());
        }
        if (exclude.length > 0) {
            return !this.hasAssignableFrom(exclude, throwable.getClass());
        }
        return true;
    }

    private void outTimes(Retry retry, Throwable throwable, int currentTimes) {
        if (currentTimes > retry.times()) {
            throw new ObjectRetrievalFailureException(retry.message(), throwable);
        }
    }

    private boolean hasAssignableFrom(Class<? extends Throwable>[] cls,
            Class<? extends Throwable> clz) {
        for (Class<? extends Throwable> cl : cls) {
            if (cl.isAssignableFrom(clz)) {
                return true;
            }
        }
        return false;
    }
    
}
