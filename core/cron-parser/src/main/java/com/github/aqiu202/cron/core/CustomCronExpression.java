package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.time.DateTime;
import com.github.aqiu202.cron.time.SimpleDateTime;

import java.util.Date;

public class CustomCronExpression extends AbstractCustomCronExpression<Date, Date> {

    public CustomCronExpression(String cron) {
        super(cron);
    }

    @Override
    protected DateTime<Date> createDateTimeInstance(Date date) {
        return new SimpleDateTime(date);
    }
}
