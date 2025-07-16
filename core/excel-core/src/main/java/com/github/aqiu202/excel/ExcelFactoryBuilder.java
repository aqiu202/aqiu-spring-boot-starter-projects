package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;

public class ExcelFactoryBuilder {

    private ConverterFactory converterFactory = new SimpleConverterFactory();

    public ExcelFactoryBuilder converterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

    public ExcelFactory build() {
        return new SimpleExcelFactory(this.converterFactory);
    }

}
