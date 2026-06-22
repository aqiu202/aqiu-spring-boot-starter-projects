package com.github.aqiu202.cron.time;

import com.github.aqiu202.cron.core.CronConstants;

public interface DateTimeDefinition {

    int getSecond();

    int getMinute();

    int getHour();

    int getDayOfMonth();

    int getMonth();

    int getYear();

    default int get(int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                return this.getSecond();
            case CronConstants.INDEX_MINUTE:
                return this.getMinute();
            case CronConstants.INDEX_HOUR:
                return this.getHour();
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return this.getDayOfMonth();
            case CronConstants.INDEX_MONTH:
                return this.getMonth();
            case CronConstants.INDEX_DAYS_OF_WEEK:
                return 0;
            case CronConstants.INDEX_YEAR:
                return this.getYear();
            default:
                throw new IllegalArgumentException("index must be in 0-5");
        }
    }

}
