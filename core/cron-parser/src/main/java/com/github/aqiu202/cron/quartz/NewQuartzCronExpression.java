package com.github.aqiu202.cron.quartz;

import com.github.aqiu202.cron.core.AbstractCronExpression;

import java.text.ParseException;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class NewQuartzCronExpression extends AbstractCronExpression<Clock, ZonedDateTime> {

    private final QuartzCronExpression expression;

    public NewQuartzCronExpression(String expression) {
        super(expression);
        try {
            this.expression = new QuartzCronExpression(expression);
        } catch (ParseException e) {
            throw new RuntimeException("cron表达式格式异常", e);
        }
    }

    @Override
    public ZonedDateTime nextExecution(Clock date) {
        Date input = this.convertParam(date);
        Date result = this.expression.getNextValidTimeAfter(input);
        return this.convertResult(result);
    }

    private Date convertParam(Clock clock) {
        return Date.from(clock.instant());
    }

    private ZonedDateTime convertResult(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault());
    }
}
