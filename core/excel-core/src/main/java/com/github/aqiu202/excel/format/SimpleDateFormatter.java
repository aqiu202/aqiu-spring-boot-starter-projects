package com.github.aqiu202.excel.format;

import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class SimpleDateFormatter implements DateFormatter {
    @Override
    public String format(Object date, String pattern) {
        // Jdk8之前日期类型的支持
        if (date instanceof Date) {
            return new SimpleDateFormat(pattern).format((Date) date);
        }
        // 对Jdk8新的时间API的支持
        if (date instanceof TemporalAccessor) {
            return DateTimeFormatter.ofPattern(pattern).format(((TemporalAccessor) date));
        }
        // 未知的类型处理
        return date.toString();
    }

    @Override
    public <S> S parse(String text, String pattern, Class<S> targetType) {
        if (ClassUtils.isAssignableFrom(Date.class, targetType)) {
            try {
                return (S) new SimpleDateFormat(pattern).parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (ClassUtils.isAssignableFrom(TemporalAccessor.class, targetType)) {
            Method parseMethod;
            try {
                parseMethod = targetType.getMethod("parse", CharSequence.class, DateTimeFormatter.class);
            } catch (NoSuchMethodException e) {
                return null;
            }
            return (S) ReflectionUtils.invokeMethod(parseMethod, null, text, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }
}
