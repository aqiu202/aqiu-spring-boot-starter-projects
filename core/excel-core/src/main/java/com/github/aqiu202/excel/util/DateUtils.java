package com.github.aqiu202.excel.util;

import com.github.aqiu202.util.ClassUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateUtils {

    public static boolean isDate(Class<?> type) {
        return ClassUtils.isAssignableFrom(java.sql.Date.class, type) || LocalDate.class.equals(type);
    }

    public static boolean isTime(Class<?> type) {
        return ClassUtils.isAssignableFrom(java.sql.Time.class, type) || LocalTime.class.equals(type);
    }

    public static boolean isDateTime(Class<?> type) {
        return Date.class.equals(type) || LocalDateTime.class.equals(type);
    }

    public static boolean isDate(Object target) {
        if (target == null) {
            return false;
        }
        return isDate(target.getClass());
    }

    public static boolean isTime(Object target) {
        if (target == null) {
            return false;
        }
        return isTime(target.getClass());
    }

    public static boolean isDateTime(Object target) {
        if (target == null) {
            return false;
        }
        return isDateTime(target.getClass());
    }

}
