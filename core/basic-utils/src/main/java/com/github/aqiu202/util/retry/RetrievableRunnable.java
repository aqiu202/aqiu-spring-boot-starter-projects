package com.github.aqiu202.util.retry;

import com.github.aqiu202.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public interface RetrievableRunnable extends Runnable {

    Logger log = LoggerFactory.getLogger(RetrievableRunnable.class);

    @Override
    default void run() {
        try {
            this.runOneTime(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default void runOneTime(int currentTime) {
        try {
            this.getRunnable().run();
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
                this.runOneTime(++currentTime);
            } else {
                throw e;
            }
        }
    }

    default boolean canRry(Throwable e) {
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

    default boolean hasAssignableFrom(Collection<Class<? extends Throwable>> cls,
                                      Class<? extends Throwable> clz) {
        for (Class<? extends Throwable> cl : cls) {
            if (cl.isAssignableFrom(clz)) {
                return true;
            }
        }
        return false;
    }

    @Nonnull
    Runnable getRunnable();

    default int getMaxTryTimes() {
        return 3;
    }

    default Collection<Class<? extends Throwable>> includes() {
        return Collections.emptyList();
    }

    default Collection<Class<? extends Throwable>> excludes() {
        return Collections.emptyList();
    }

    default long getDelay() {
        return 0;
    }

    default TimeUnit getDelayTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }

}
