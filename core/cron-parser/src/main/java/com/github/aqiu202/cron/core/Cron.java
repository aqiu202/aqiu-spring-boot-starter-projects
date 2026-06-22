package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.field.CronField;
import com.github.aqiu202.cron.field.EnumerableCronField;

public interface Cron {

    EnumerableCronField getSeconds();

    EnumerableCronField getMinutes();

    EnumerableCronField getHours();

    CronField getDaysOfMonth();

    EnumerableCronField getMonths();

    CronField getDaysOfWeek();

    EnumerableCronField getYears();

}
