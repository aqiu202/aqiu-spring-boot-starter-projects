package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.core.ValueResult;
import com.github.aqiu202.cron.time.DateTime;

import java.util.Collections;

public class UnknownCronField extends EnumerableCronField {

    public UnknownCronField(int index) {
        super(index, Collections.emptyList());
    }

    @Override
    public ValueResult findAfter(int value, DateTime<?> c) {
        throw new UnsupportedOperationException();
    }
}
