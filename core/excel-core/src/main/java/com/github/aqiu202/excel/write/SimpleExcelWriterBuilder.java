package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;

public class SimpleExcelWriterBuilder extends ExcelWriterBuilder {

    public SimpleExcelWriterBuilder(ConverterFactory converterFactory) {
        super(converterFactory);
    }

    @Override
    public ExcelWriter build() {
        return new SimpleExcelWriter(this.configuration, this.converterFactory);
    }
}
