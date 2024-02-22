package com.github.aqiu202.excel.format;

import com.github.aqiu202.util.ClassUtils;

public class SimpleNullFormatter implements NullFormatter {
    @Override
    public String format() {
        return "";
    }

    @Override
    public Object parse(Class<?> targetType) {
        if (targetType.isPrimitive()) {
            if (ClassUtils.isNumber(targetType)) {
                return 0;
            }
            if (targetType.equals(boolean.class)) {
                return false;
            }
            if (targetType.equals(char.class)) {
                return '\0';
            }
        }
        return null;
    }
}
