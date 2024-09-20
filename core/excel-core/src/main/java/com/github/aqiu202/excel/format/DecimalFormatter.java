package com.github.aqiu202.excel.format;

import java.text.DecimalFormat;

public class DecimalFormatter extends AbstractNumberFormatter {
    @Override
    public String format(Number number, String pattern) {
        return new DecimalFormat(pattern).format(number.doubleValue());
    }

}
