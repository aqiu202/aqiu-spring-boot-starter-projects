package com.github.aqiu202.cron.quartz;

import com.github.aqiu202.cron.core.AbstractCronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * 基于Quartz算法的Cron表达式解析
 */
public class DateQuartzCronExpression extends AbstractCronExpression<Date, Date> {

    private final QuartzCronExpression expression;

    public DateQuartzCronExpression(String expression) {
        super(expression);
        try {
            this.expression = new QuartzCronExpression(expression);
        } catch (ParseException e) {
            throw new RuntimeException("cron表达式格式异常", e);
        }
    }

    @Override
    public Date nextExecution(Date date) {
        return this.expression.getNextValidTimeAfter(date);
    }
}
