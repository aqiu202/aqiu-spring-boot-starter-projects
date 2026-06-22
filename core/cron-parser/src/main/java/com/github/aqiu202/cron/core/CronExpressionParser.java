package com.github.aqiu202.cron.core;

public interface CronExpressionParser<S, T> {

    CronExpression<S, T> parser(String expression);
}
