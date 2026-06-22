package com.github.aqiu202.cron.field;

public class BaseCronField implements CronField {

    protected final int index;

    protected BaseCronField(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

}
