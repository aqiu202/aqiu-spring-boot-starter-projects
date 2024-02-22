package com.github.aqiu202.excel.model;

import com.github.aqiu202.excel.format.DateFormatter;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.NullFormatter;
import com.github.aqiu202.excel.format.NumberFormatter;

public class SheetDataConfiguration implements FormatterProvider {

    private String dateFormat;
    private Class<? extends DateFormatter> dateFormatter = DEFAULT_DATE_FORMATTER;
    private String numberFormat;
    private Class<? extends NumberFormatter> numberFormatter = DEFAULT_NUM_FORMATTER;
    private Class<? extends NullFormatter> nullFormatter = DEFAULT_NULL_FORMATTER;

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public Class<? extends NullFormatter> getNullFormatter() {
        return nullFormatter;
    }

    public void setNullFormatter(Class<? extends NullFormatter> nullFormatter) {
        this.nullFormatter = nullFormatter;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Class<? extends DateFormatter> getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(Class<? extends DateFormatter> dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Class<? extends NumberFormatter> getNumberFormatter() {
        return numberFormatter;
    }

    public void setNumberFormatter(Class<? extends NumberFormatter> numberFormatter) {
        this.numberFormatter = numberFormatter;
    }
}
