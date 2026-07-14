package com.github.aqiu202.util.retry;

import com.github.aqiu202.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RetrievableCallable<T> implements Callable<T> {

    private static final Logger log = LoggerFactory.getLogger(RetrievableCallable.class);
    private final Callable<T> callable;
    private int maxTryTimes = 3;
    private List<Class<? extends Throwable>> includes = Collections.emptyList();
    private List<Class<? extends Throwable>> excludes = Collections.emptyList();
    private long delay = 0;
    private TimeUnit delayTimeUnit = TimeUnit.MILLISECONDS;

    public RetrievableCallable(Callable<T> callable) {
        this.callable = callable;
    }

    @Nonnull
    public Callable<T> getCallable() {
        return this.callable;
    }

    public RetrievableCallable<T> setIncludes(List<Class<? extends Throwable>> includes) {
        this.includes = includes;
        return this;
    }

    public RetrievableCallable<T> setExcludes(List<Class<? extends Throwable>> excludes) {
        this.excludes = excludes;
        return this;
    }

    public int getMaxTryTimes() {
        return maxTryTimes;
    }

    public RetrievableCallable<T> setMaxTryTimes(int maxTryTimes) {
        this.maxTryTimes = maxTryTimes;
        return this;
    }

    public RetrievableCallable<T> setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public RetrievableCallable<T> setDelayTimeUnit(TimeUnit delayTimeUnit) {
        this.delayTimeUnit = delayTimeUnit;
        return this;
    }

    @Override
    public T call() {
        try {
            return this.runOneTime(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T runOneTime(int currentTime) throws Exception {
        try {
            return this.getCallable().call();
        } catch (Exception e) {
            log.warn("任务执行异常，正在尝试重试", e);
            if (this.canRry(e)) {
                int maxTryTimes = Math.max(0, this.getMaxTryTimes());
                if (maxTryTimes == 0) {
                    throw e;
                }
                if (currentTime > maxTryTimes) {
                    throw new OutNumberOfTimesException("超出次数限制", e);
                }
                log.warn("尝试重试，最大重试次数：{}，当前重试次数：{}", maxTryTimes, currentTime);
                final long delay = this.getDelay();
                if (delay > 0) {
                    try {
                        this.getDelayTimeUnit().sleep(delay);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("线程休眠时遇到中断异常", ex);
                    }
                }
                return this.runOneTime(++currentTime);
            } else {
                throw e;
            }
        }
    }

    private boolean canRry(Throwable e) {
        Collection<Class<? extends Throwable>> include = this.includes();
        Collection<Class<? extends Throwable>> exclude = this.excludes();
        if (!CollectionUtils.isEmpty(include)) {
            return this.hasAssignableFrom(include, e.getClass());
        }
        if (!CollectionUtils.isEmpty(exclude)) {
            return !this.hasAssignableFrom(exclude, e.getClass());
        }
        return true;
    }

    protected boolean hasAssignableFrom(Collection<Class<? extends Throwable>> cls,
        Class<? extends Throwable> clz) {
        for (Class<? extends Throwable> cl : cls) {
            if (cl.isAssignableFrom(clz)) {
                return true;
            }
        }
        return false;
    }


    public Collection<Class<? extends Throwable>> includes() {
        return this.includes;
    }

    public Collection<Class<? extends Throwable>> excludes() {
        return this.excludes;
    }

    public long getDelay() {
        return this.delay;
    }

    public TimeUnit getDelayTimeUnit() {
        return this.delayTimeUnit;
    }
}
