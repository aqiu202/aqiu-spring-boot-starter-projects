package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.core.CronConstants;
import com.github.aqiu202.cron.exp.InvalidCronException;
import com.github.aqiu202.cron.util.CronUtils;

import java.util.Arrays;
import java.util.List;

public class StepCronField extends EnumerableCronField {

    public StepCronField(int index, int start, int step) {
        super(index, parseStepValues(start, step, index));
        this.start = start;
        this.step = step;
    }

    private final int start;
    private final int step;

    public int getStart() {
        return start;
    }

    public int getStep() {
        return step;
    }

    private static List<Integer> parseStepValues(int start, int step, int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                return stepSeconds(start, step);
            case CronConstants.INDEX_MINUTE:
                return stepMinutes(start, step);
            case CronConstants.INDEX_HOUR:
                return stepHours(start, step);
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return stepDaysOfMonth(start, step);
            case CronConstants.INDEX_MONTH:
                return stepMonths(start, step);
            case CronConstants.INDEX_DAYS_OF_WEEK:
                return stepDaysOfWeek(start, step);
            case CronConstants.INDEX_YEAR:
                CronUtils.checkYears(start);
                return stepWithPeriod(start, step, CronConstants.MAX_YEAR + 1);
        }
        return null;
    }

    private static List<Integer> stepSeconds(int start, int step) {
        CronUtils.checkSeconds(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_SECOND);
    }

    private static List<Integer> stepMinutes(int start, int step) {
        CronUtils.checkMinutes(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_MINUTE);
    }

    private static List<Integer> stepHours(int start, int step) {
        CronUtils.checkHours(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_HOUR);
    }

    private static List<Integer> stepDaysOfMonth(int start, int step) {
        CronUtils.checkDaysOfMonths(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_DAYS_OF_MONTH + 1);
    }

    private static List<Integer> stepMonths(int start, int step) {
        CronUtils.checkMonths(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_MONTH + 1);
    }

    private static List<Integer> stepDaysOfWeek(int start, int step) {
        CronUtils.checkDaysOfWeeks(start, step);
        return stepWithPeriod(start, step, CronConstants.PERIOD_DAYS_OF_WEEK + 1);
    }

    private static List<Integer> stepWithPeriod(int start, int step, int period) {
        if (step == 0) {
            throw new InvalidCronException("间隔不能为0");
        }
        int length = (period - start - 1) / step;
        Integer[] arr = new Integer[length + 1];
        for (int i = start, index = 0; i < period; i += step, index++) {
            arr[index] = i;
        }
        return Arrays.asList(arr);
    }
}
