package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.read.SimpleExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;
import com.github.aqiu202.excel.write.SimpleExcelWriterBuilder;

public class SimpleExcelFactory implements ExcelFactory {

    private final ConverterFactory converterFactory;

    public SimpleExcelFactory() {
        this(new SimpleConverterFactory());
    }

    public SimpleExcelFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public ExcelReaderBuilder buildReader() {
        return new SimpleExcelReaderBuilder(this.getConverterFactory());
    }

    @Override
    public ExcelWriterBuilder buildWriter() {
        return new SimpleExcelWriterBuilder(this.getConverterFactory());
    }

    public ConverterFactory getConverterFactory() {
        return this.converterFactory;
    }
}