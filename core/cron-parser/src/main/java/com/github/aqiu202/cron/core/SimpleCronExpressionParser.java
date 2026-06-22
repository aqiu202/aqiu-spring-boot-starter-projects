package com.github.aqiu202.cron.core;

import java.time.Clock;
import java.time.ZonedDateTime;

public class SimpleCronExpressionParser implements CronExpressionParser<Clock, ZonedDateTime> {
    @Override
    public CronExpression<Clock, ZonedDateTime> parser(String expression) {
        return new NewCronExpression(expression);
    }
}
