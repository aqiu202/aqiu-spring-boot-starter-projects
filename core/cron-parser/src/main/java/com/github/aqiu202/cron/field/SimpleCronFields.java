package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.exp.InvalidCronException;

import java.util.*;

public class SimpleCronFields extends CronFields<EnumerableCronField> {

    private final int index;

    public SimpleCronFields(int index) {
        this.index = index;
    }

    @Override
    public EnumerableCronField merge() {
        if (this.isEmpty()) {
            throw new InvalidCronException("cron格式异常");
        }
        Set<Integer> s = new HashSet<>();
        for (EnumerableCronField value : this) {
            List<Integer> vs = value.getValues();
            if (vs != null && !vs.isEmpty()) {
                s.addAll(vs);
            }
        }
        List<Integer> values = new ArrayList<>(s);
        values.sort(Comparator.naturalOrder());
        return new EnumerableCronField(this.index, values);
    }
}
