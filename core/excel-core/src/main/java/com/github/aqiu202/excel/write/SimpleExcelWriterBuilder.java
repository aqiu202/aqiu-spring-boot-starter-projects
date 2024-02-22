package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.ExcelFactory;

public class SimpleExcelWriterBuilder extends ExcelWriterBuilder {

    private final ExcelFactory excelFactory;

    public SimpleExcelWriterBuilder(ExcelFactory excelFactory) {
        this.excelFactory = excelFactory;
    }

    public ExcelFactory getExcelFactory() {
        return excelFactory;
    }

    @Override
    public ExcelWriter build() {
        return new SimpleExcelWriter(this.getConfiguration(), this.getExcelFactory().getConverterFactory());
    }
}
