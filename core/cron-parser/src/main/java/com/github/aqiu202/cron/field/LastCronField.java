package com.github.aqiu202.cron.field;

public class LastCronField extends BaseCronField {

    private final int value;

    public LastCronField(int index, int value) {
        super(index);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
