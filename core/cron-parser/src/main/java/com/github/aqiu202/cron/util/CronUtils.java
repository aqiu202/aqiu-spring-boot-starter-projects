package com.github.aqiu202.cron.util;

import com.github.aqiu202.cron.core.CronConstants;
import com.github.aqiu202.cron.exp.InvalidCronException;

public abstract class CronUtils {

    public static boolean isLeapYear(final int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static int getLastDayOfMonth(final int month, final int year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                throw new IllegalArgumentException("Illegal month number: "
                        + month);
        }
    }

    public static void checkSeconds(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_SECOND || val > CronConstants.MAX_SECOND) {
                throw new InvalidCronException(String.format("秒数超出范围%d-%d", CronConstants.MIN_SECOND, CronConstants.MAX_SECOND));
            }
        }
    }

    public static void checkMinutes(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_MINUTE || val > CronConstants.MAX_MINUTE) {
                throw new InvalidCronException(String.format("分钟数超出范围%d-%d", CronConstants.MIN_MINUTE, CronConstants.MAX_MINUTE));
            }
        }
    }

    public static void checkHours(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_HOUR || val > CronConstants.MAX_HOUR) {
                throw new InvalidCronException(String.format("小时数超出范围%d-%d", CronConstants.MIN_HOUR, CronConstants.MAX_HOUR));
            }
        }
    }

    public static void checkDaysOfMonths(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_DAYS_OF_MONTH || val > CronConstants.MAX_DAYS_OF_MONTH) {
                throw new InvalidCronException(String.format("DaysOfMonth超出范围%d-%d", CronConstants.MIN_DAYS_OF_MONTH, CronConstants.MAX_DAYS_OF_MONTH));
            }
        }
    }

    public static void checkMonths(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_MONTH || val > CronConstants.MAX_MONTH) {
                throw new InvalidCronException(String.format("月份数超出范围%d-%d", CronConstants.MIN_MONTH, CronConstants.MAX_MONTH));
            }
        }
    }

    public static void checkDaysOfWeeks(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_DAYS_OF_WEEK || val > CronConstants.MAX_DAYS_OF_WEEK) {
                throw new InvalidCronException(String.format("DaysOfWeek超出范围%d-%d", CronConstants.MIN_DAYS_OF_WEEK, CronConstants.MAX_DAYS_OF_WEEK));
            }
        }
    }

    public static void checkYears(int... vals) {
        for (int val : vals) {
            if (val < CronConstants.MIN_YEAR || val > CronConstants.MAX_YEAR) {
                throw new InvalidCronException(String.format("年份数超出范围%d-%d", CronConstants.MIN_YEAR, CronConstants.MAX_YEAR));
            }
        }
    }
    
    public static int findMinValue(int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                return CronConstants.MIN_SECOND;
            case CronConstants.INDEX_MINUTE:
                return CronConstants.MIN_MINUTE;
            case CronConstants.INDEX_HOUR:
                return CronConstants.MIN_HOUR;
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return CronConstants.MIN_DAYS_OF_MONTH;
            case CronConstants.INDEX_MONTH:
                return CronConstants.MIN_MONTH;
        }
        return 0;
    }

    public static boolean isWeekday(int dayOfWeek) {
        return dayOfWeek > 1 && dayOfWeek < 7;
    }
}
