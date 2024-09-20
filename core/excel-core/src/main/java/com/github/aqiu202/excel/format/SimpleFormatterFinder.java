package com.github.aqiu202.excel.format;

import com.github.aqiu202.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleFormatterFinder implements FormatterFinder {

    private final Map<Class<?>, Object> formatterMap = new ConcurrentHashMap<>();

    public <T> T findFormatter(Class<T> formatterType) {
        return (T) formatterMap.compute(formatterType, (type, value) -> {
            if (value == null) {
                value = ClassUtils.newInstance(formatterType);
            }
            return value;
        });
    }
}
