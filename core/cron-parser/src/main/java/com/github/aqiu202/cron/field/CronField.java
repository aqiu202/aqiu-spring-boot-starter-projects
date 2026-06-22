package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.util.CronUtils;

public interface CronField {

    int getIndex();

    default int firstValue() {
        return CronUtils.findMinValue(this.getIndex());
    }

}
