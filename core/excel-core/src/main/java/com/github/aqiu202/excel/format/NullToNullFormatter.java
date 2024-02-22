package com.github.aqiu202.excel.format;

public class NullToNullFormatter implements NullFormatter {
    @Override
    public String format() {
        return "";
    }

    @Override
    public Object parse(Class<?> targetType) {
        return null;
    }
}
