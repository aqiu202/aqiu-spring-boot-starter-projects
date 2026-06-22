package com.github.aqiu202.cron.field;

public class WeekdayCronField extends BaseCronField {

    private final int value;

    public WeekdayCronField(int index, int value) {
        super(index);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
