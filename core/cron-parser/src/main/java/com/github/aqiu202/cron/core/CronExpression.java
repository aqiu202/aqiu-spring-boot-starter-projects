package com.github.aqiu202.cron.core;

/**
 * Cron表达式
 */
public interface CronExpression<S,T> {

    String getExpression();

    T nextExecution(S s);
}
