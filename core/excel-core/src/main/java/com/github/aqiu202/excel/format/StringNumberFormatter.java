package com.github.aqiu202.excel.format;

public class StringNumberFormatter extends AbstractNumberFormatter {
    @Override
    public String format(Number number, String pattern) {
        return String.format(pattern, number.doubleValue());
    }

}
