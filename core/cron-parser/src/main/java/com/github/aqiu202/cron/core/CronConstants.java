package com.github.aqiu202.cron.core;

public abstract class CronConstants {
    public static final int INDEX_SECOND = 0;
    public static final int INDEX_MINUTE = 1;
    public static final int INDEX_HOUR = 2;
    public static final int INDEX_DAYS_OF_MONTH = 3;
    public static final int INDEX_MONTH = 4;
    public static final int INDEX_DAYS_OF_WEEK = 5;
    public static final int INDEX_YEAR = 6;
    public static final char SEPARATOR_RANGE = '-';
    public static final char SEPARATOR_ENUM = ',';
    public static final char SEPARATOR_STEP = '/';
    public static final char SEPARATOR_INDEX = '#';

    public static final char SEPARATOR_LAST = 'L';

    public static final char SEPARATOR_WEEKDAY = 'W';

    public static final String VAL_EVERY = "*";
    public static final String VAL_UNKNOWN = "?";
    public static final String VAL_LAST = String.valueOf(SEPARATOR_LAST);
    public static final String VAL_WEEKDAY = String.valueOf(SEPARATOR_WEEKDAY);

    public static final String VAL_LAST_WEEKDAY = VAL_LAST + VAL_WEEKDAY;

    public static final int MIN_SECOND = 0;
    public static final int MAX_SECOND = 59;

    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 59;
    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_DAYS_OF_MONTH = 1;
    public static final int MAX_DAYS_OF_MONTH = 31;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MIN_DAYS_OF_WEEK = 1;
    public static final int MAX_DAYS_OF_WEEK = 7;
    public static final int MIN_YEAR = 1970;
    public static final int MAX_YEAR = 2999;

    public static final int PERIOD_SECOND = MAX_SECOND + 1;
    public static final int PERIOD_MINUTE = MAX_MINUTE + 1;
    public static final int PERIOD_HOUR = MAX_HOUR + 1;
    public static final int PERIOD_MONTH = MAX_MONTH;
    public static final int PERIOD_DAYS_OF_MONTH = MAX_DAYS_OF_MONTH;
    public static final int PERIOD_DAYS_OF_WEEK = MAX_DAYS_OF_WEEK;

}
