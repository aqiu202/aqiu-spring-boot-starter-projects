package com.github.aqiu202.util.retry;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrievableTask implements RetrievableRunnable {

    private final Runnable runnable;
    private int maxTryTimes = 3;
    private List<Class<? extends Throwable>> includes = Collections.emptyList();
    private List<Class<? extends Throwable>> excludes = Collections.emptyList();
    private long delay = 0;
    private TimeUnit delayTimeUnit = TimeUnit.MILLISECONDS;

    public RetrievableTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Nonnull
    @Override
    public Runnable getRunnable() {
        return this.runnable;
    }

    public RetrievableTask setIncludes(List<Class<? extends Throwable>> includes) {
        this.includes = includes;
        return this;
    }

    public RetrievableTask setExcludes(List<Class<? extends Throwable>> excludes) {
        this.excludes = excludes;
        return this;
    }

    @Override
    public int getMaxTryTimes() {
        return maxTryTimes;
    }

    public RetrievableTask setMaxTryTimes(int maxTryTimes) {
        this.maxTryTimes = maxTryTimes;
        return this;
    }

    public RetrievableTask setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public RetrievableTask setDelayTimeUnit(TimeUnit delayTimeUnit) {
        this.delayTimeUnit = delayTimeUnit;
        return this;
    }

    @Override
    public Collection<Class<? extends Throwable>> includes() {
        return this.includes;
    }

    @Override
    public Collection<Class<? extends Throwable>> excludes() {
        return this.excludes;
    }

    @Override
    public long getDelay() {
        return this.delay;
    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return this.delayTimeUnit;
    }
}
