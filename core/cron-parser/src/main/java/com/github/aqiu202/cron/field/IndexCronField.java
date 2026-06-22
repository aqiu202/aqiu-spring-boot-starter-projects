package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.core.CronConstants;
import com.github.aqiu202.cron.exp.InvalidCronException;
import com.github.aqiu202.cron.util.CronUtils;

public class IndexCronField extends BaseCronField {

    private final int value;
    private final int count;

    public IndexCronField(int index, int value, int count) {
        super(index);
        CronUtils.checkDaysOfWeeks(value);
        if (count < 1 || count > 5) {
            throw new InvalidCronException(String.format("表达式异常--%s后面数字必须介于1-5之间", CronConstants.SEPARATOR_INDEX));
        }
        this.value = value;
        this.count = count;
    }

    public int getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }


}
