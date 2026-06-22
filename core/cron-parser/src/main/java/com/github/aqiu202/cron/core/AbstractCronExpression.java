package com.github.aqiu202.cron.core;

public abstract class AbstractCronExpression<S, T> implements CronExpression<S, T> {

    protected AbstractCronExpression(String expression) {
        this.expression = expression;
    }

    protected final String expression;

    @Override
    public String getExpression() {
        return expression;
    }

}
