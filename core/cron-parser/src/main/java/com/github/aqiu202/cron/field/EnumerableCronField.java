package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.core.CronConstants;
import com.github.aqiu202.cron.core.ValueResult;
import com.github.aqiu202.cron.exp.NoValidValueException;
import com.github.aqiu202.cron.time.DateTime;
import com.github.aqiu202.cron.util.CronUtils;

import java.util.Arrays;
import java.util.List;

public class EnumerableCronField extends BaseCronField {

    public EnumerableCronField(int index, List<Integer> values) {
        super(index);
        this.values = values;
    }

    public EnumerableCronField(int index, Integer... values) {
        this(index, Arrays.asList(values));
    }

    protected final List<Integer> values;

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public int firstValue() {
        if (!values.isEmpty()) {
            return values.get(0);
        }
        return super.firstValue();
    }

    public ValueResult findAfter(int value, DateTime<?> c) {
        int maxDays = 0;
        if (index == CronConstants.INDEX_DAYS_OF_MONTH || index == CronConstants.INDEX_MONTH || index == CronConstants.INDEX_YEAR) {
            maxDays = CronUtils.getLastDayOfMonth(c.getMonth(), c.getYear());
        }
        for (Integer val : values) {
            if (index == CronConstants.INDEX_DAYS_OF_MONTH) {
                if (value > maxDays) {
                    break;
                }
                if (val >= value) {
                    return new ValueResult(val, false, val <= maxDays);
                }
            } else if (index == CronConstants.INDEX_MONTH) {
                if (val >= value) {
                    maxDays = CronUtils.getLastDayOfMonth(val, c.getYear());
                    return new ValueResult(val, false, c.getDayOfMonth() <= maxDays);
                }
            } else if (index == CronConstants.INDEX_YEAR) {
                if (val >= value) {
                    maxDays = CronUtils.getLastDayOfMonth(c.getMonth(), val);
                    return new ValueResult(val, false, c.getDayOfMonth() <= maxDays);
                }
            }else if (val >= value) {
                return new ValueResult(val);
            }
        }
        if (index == CronConstants.INDEX_YEAR) {
            // 如果年份没有匹配到，直接报异常
            throw new NoValidValueException("不存在匹配的时间");
        }
        return new ValueResult(this.firstValue(), true);
    }

}
