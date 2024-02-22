package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.model.SheetDataConfiguration;
import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.read.SimpleExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;
import com.github.aqiu202.excel.write.SimpleExcelWriterBuilder;

public class SimpleExcelFactory implements ExcelFactory {
    @Override
    public SheetDataConfiguration getConfiguration() {
        // TODO 读写的全局配置暂未支持
        return null;
    }

    private ConverterFactory converterFactory = SimpleConverterFactory.INSTANCE;

    @Override
    public ExcelReaderBuilder forReader() {
        return new SimpleExcelReaderBuilder();
    }

    @Override
    public ExcelWriterBuilder forWriter() {
        return new SimpleExcelWriterBuilder(this);
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
