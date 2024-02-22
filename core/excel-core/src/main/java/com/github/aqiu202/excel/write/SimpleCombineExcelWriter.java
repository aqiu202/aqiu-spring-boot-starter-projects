package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.appender.ExcelAppender;
import com.github.aqiu202.excel.appender.SimpleExcelAppender;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public class SimpleCombineExcelWriter<T extends CombineExcelWriter<?>> implements CombineExcelWriter<T> {

    private final ExcelAppender excelAppender;

    public SimpleCombineExcelWriter() {
        this.excelAppender = new SimpleExcelAppender();
    }

    public SimpleCombineExcelWriter(DataExtractor<?> dataExtractor) {
        this.excelAppender = new SimpleExcelAppender(dataExtractor);
    }
    public SimpleCombineExcelWriter(DataExtractor<?> dataExtractor, ConverterFactory converterFactory) {
        this.excelAppender = new SimpleExcelAppender(dataExtractor, converterFactory);
    }

    @Override
    public T dataExtractor(DataExtractor<?> dataReader) {
        this.excelAppender.dataExtractor(dataReader);
        return (T) this;
    }

    @Override
    public DataExtractor<?> getDataExtractor() {
        return this.excelAppender.getDataExtractor();
    }

    @Override
    public SheetWriteConfiguration getConfiguration() {
        return this.excelAppender.getConfiguration();
    }

    @Override
    public T configuration(SheetWriteConfiguration configuration) {
        this.excelAppender.configuration(configuration);
        return (T) this;
    }

    @Override
    public ExcelAppender getExcelAppender() {
        return excelAppender;
    }


    @Override
    public T converterFactory(ConverterFactory converterFactory) {
        this.getExcelAppender().converterFactory(converterFactory);
        return (T) this;
    }

    @Override
    public ConverterFactory getConverterFactory() {
        return this.getExcelAppender().getConverterFactory();
    }
}
