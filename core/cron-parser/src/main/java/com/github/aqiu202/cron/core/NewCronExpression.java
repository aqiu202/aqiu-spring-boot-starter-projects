package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.time.DateTime;
import com.github.aqiu202.cron.time.NewDateTime;

import java.time.Clock;
import java.time.ZonedDateTime;

public class NewCronExpression extends AbstractCustomCronExpression<Clock, ZonedDateTime> {

    public NewCronExpression(String cron) {
        super(cron);
    }

    public ZonedDateTime nextExecution(ZonedDateTime time) {
        return this.processTime(new NewDateTime(time));
    }

    @Override
    protected DateTime<ZonedDateTime> createDateTimeInstance(Clock clock) {
        return new NewDateTime(clock);
    }

}
