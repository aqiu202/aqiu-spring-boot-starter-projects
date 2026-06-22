package com.github.aqiu202.cron.field;

import com.github.aqiu202.cron.core.CronConstants;
import com.github.aqiu202.cron.exp.InvalidCronException;
import com.github.aqiu202.cron.util.CronUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RangeCronField extends EnumerableCronField {

    public RangeCronField(int index, int start, int end) {
        super(index, parseRangeValues(start, end, index));
        this.start = start;
        this.end = end;
    }

    private final int start;
    private final int end;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    private static List<Integer> parseRangeValues(int start, int end, int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                return rangeSeconds(start, end);
            case CronConstants.INDEX_MINUTE:
                return rangeMinutes(start, end);
            case CronConstants.INDEX_HOUR:
                return rangeHours(start, end);
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return rangeDaysOfMonth(start, end);
            case CronConstants.INDEX_MONTH:
                return rangeMonths(start, end);
            case CronConstants.INDEX_DAYS_OF_WEEK:
                return rangeDaysOfWeek(start, end);
            case CronConstants.INDEX_YEAR:
                CronUtils.checkYears(start, end);
                if (start > end) {
                    throw new InvalidCronException("表达式格式异常--开始年份不能大于截至年份");
                }
                return rangeSimple(start, end);
        }
        return null;
    }

    private static List<Integer> rangeSeconds(int start, int end) {
        CronUtils.checkSeconds(start, end);
        return rangeWithPeriod(start, end, CronConstants.PERIOD_SECOND);
    }

    private static List<Integer> rangeMinutes(int start, int end) {
        CronUtils.checkMinutes(start, end);
        return rangeWithPeriod(start, end, CronConstants.PERIOD_MINUTE);
    }

    private static List<Integer> rangeHours(int start, int end) {
        CronUtils.checkHours(start, end);
        return rangeWithPeriod(start, end, CronConstants.PERIOD_HOUR);
    }

    private static List<Integer> rangeDaysOfMonth(int start, int end) {
        CronUtils.checkDaysOfMonths(start, end);
        return rangeWithScope(start, end, CronConstants.MIN_DAYS_OF_MONTH, CronConstants.MAX_DAYS_OF_MONTH);
    }

    private static List<Integer> rangeMonths(int start, int end) {
        CronUtils.checkMonths(start, end);
        return rangeWithScope(start, end, CronConstants.MIN_MONTH, CronConstants.MAX_MONTH);
    }

    private static List<Integer> rangeDaysOfWeek(int start, int end) {
        CronUtils.checkDaysOfWeeks(start, end);
        return rangeWithScope(start, end, CronConstants.MIN_DAYS_OF_WEEK, CronConstants.MAX_DAYS_OF_WEEK);
    }

    private static List<Integer> rangeWithScope(int start, int end, int min, int max) {
        if (end >= start) {
            return rangeSimple(start, end);
        } else {
            int period = max - min + 1;
            end += period;
            List<Integer> list = new ArrayList<>(end - start + 1);
            for (int i = start - min; i < end; i++) {
                list.add(min + i % period);
            }
            list.sort(Comparator.naturalOrder());
            return list;
        }
    }

    private static List<Integer> rangeWithPeriod(int start, int end, int period) {
        if (end >= start) {
            return rangeSimple(start, end);
        } else {
            end += period;
            List<Integer> list = new ArrayList<>(end - start + 1);
            for (int i = start; i <= end; i++) {
                list.add(i % period);
            }
            list.sort(Comparator.naturalOrder());
            return list;
        }
    }

    private static List<Integer> rangeSimple(int start, int end) {
        Integer[] arr = new Integer[end - start + 1];
        for (int i = start, index = 0; i <= end; i++, index++) {
            arr[index] = i;
        }
        return Arrays.asList(arr);
    }

}
