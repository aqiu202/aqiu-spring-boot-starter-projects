package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.read.SimpleExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;
import com.github.aqiu202.excel.write.SimpleExcelWriterBuilder;

public class SimpleExcelFactory implements ExcelFactory {

    private ConverterFactory converterFactory = new SimpleConverterFactory();

    @Override
    public ExcelReaderBuilder buildReader() {
        return new SimpleExcelReaderBuilder(this.getConverterFactory());
    }

    @Override
    public ExcelWriterBuilder buildWriter() {
        return new SimpleExcelWriterBuilder(this.getConverterFactory());
    }

    @Override
    public ExcelFactory converterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

    @Override
    public ConverterFactory getConverterFactory() {
        return this.converterFactory;
    }
}