package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.ConverterFactory;

public class SimpleExcelReaderBuilder extends ExcelReaderBuilder {

    public SimpleExcelReaderBuilder(ConverterFactory converterFactory) {
        super(converterFactory);
    }

    @Override
    public ExcelReader build() {
        return new SimpleExcelReader(this.converterFactory, this.configuration);
    }
}
