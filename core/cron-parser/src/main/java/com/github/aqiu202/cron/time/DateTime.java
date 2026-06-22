package com.github.aqiu202.cron.time;

import com.github.aqiu202.cron.core.Cron;
import com.github.aqiu202.cron.core.CronConstants;

public interface DateTime<T> extends DateTimeDefinition {

    void init();

    default void set(int index, int value) {
        switch (index) {
            case CronConstants.INDEX_YEAR:
                this.setYear(value);
                break;
            case CronConstants.INDEX_DAYS_OF_WEEK:
                throw new UnsupportedOperationException("daysOfWeek字段不支持set操作");
            case CronConstants.INDEX_MONTH:
                this.setMonth(value);
                break;
            case CronConstants.INDEX_DAYS_OF_MONTH:
                this.setDayOfMonth(value);
                break;
            case CronConstants.INDEX_HOUR:
                this.setHour(value);
                break;
            case CronConstants.INDEX_MINUTE:
                this.setMinute(value);
                break;
            case CronConstants.INDEX_SECOND:
                this.setSecond(value);
                break;
            default:
                throw new IllegalArgumentException("index must be in 0-6");
        }
    }

    int getDayOfWeek();

    void setSecond(int second);

    void setMinute(int minute);

    void setHour(int hour);

    void setDayOfMonth(int dayOfMonth);

    void setMonth(int month);

    void setYear(int year);

    void plusSeconds(int value);

    void plusMinutes(int value);

    void plusHours(int value);

    void plusDays(int value);

    void plusMonths(int value);

    void plusYears(int value);

    void ceilToSeconds();

    default void resetLeft(int index, Cron ci) {
        switch (index) {
            case CronConstants.INDEX_YEAR:
                this.setMonth(ci.getMonths().firstValue());
            case CronConstants.INDEX_MONTH:
                this.setDayOfMonth(ci.getDaysOfMonth().firstValue());
            case CronConstants.INDEX_DAYS_OF_MONTH:
                this.setHour(ci.getHours().firstValue());
            case CronConstants.INDEX_HOUR:
                this.setMinute(ci.getMinutes().firstValue());
            case CronConstants.INDEX_MINUTE:
                this.setSecond(ci.getSeconds().firstValue());
        }
    }

    T getTime();

    DateTimeDefinition getOrigin();

}
