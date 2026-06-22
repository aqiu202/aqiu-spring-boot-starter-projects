package com.github.aqiu202.cron.core;

public class ValueResult {
    public ValueResult(Integer value) {
        this(value, false);
    }

    public ValueResult(Integer value, boolean outOfBounds) {
        this(value, outOfBounds, true);
    }

    public ValueResult(Integer value, boolean outOfBounds, boolean valid) {
        this.value = value;
        this.outOfBounds = outOfBounds;
        this.valid = valid;
    }

    private final Integer value;
    private final boolean outOfBounds;
    private final boolean valid;

    public Integer getValue() {
        return value;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public boolean isValid() {
        return valid;
    }
}
