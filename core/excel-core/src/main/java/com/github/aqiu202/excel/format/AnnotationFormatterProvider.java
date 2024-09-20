package com.github.aqiu202.excel.format;

import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.convert.ConverterProvider;

public class AnnotationFormatterProvider implements FormatterProvider, ConverterProvider {

    private final ExcelColumn annotation;

    public AnnotationFormatterProvider(ExcelColumn annotation) {
        this.annotation = annotation;
    }

    @Override
    public Class<? extends NumberFormatter> getNumberFormatter() {
        return this.annotation.numberFormatter();
    }

    @Override
    public String getNumberFormat() {
        return this.annotation.numberFormat();
    }

    @Override
    public Class<? extends DateFormatter> getDateFormatter() {
        return this.annotation.dateFormatter();
    }

    @Override
    public String getDateFormat() {
        return this.annotation.dateFormat();
    }

    @Override
    public Class<? extends NullFormatter> getNullFormatter() {
        return this.annotation.nullFormatter();
    }

    @Override
    public String getConverter() {
        return this.annotation.converter();
    }
}
