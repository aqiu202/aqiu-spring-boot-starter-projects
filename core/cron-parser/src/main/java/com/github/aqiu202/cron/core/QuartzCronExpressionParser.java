package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.quartz.NewQuartzCronExpression;

import java.time.Clock;
import java.time.ZonedDateTime;

public class QuartzCronExpressionParser implements CronExpressionParser<Clock, ZonedDateTime> {
    @Override
    public CronExpression<Clock, ZonedDateTime> parser(String expression) {
        return new NewQuartzCronExpression(expression);
    }
}
