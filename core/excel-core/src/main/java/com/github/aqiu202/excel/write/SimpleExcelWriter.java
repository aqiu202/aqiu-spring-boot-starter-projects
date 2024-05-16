package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.write.extract.AnnotationDataExtractor;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.extract.TypeDataExtractor;

public class SimpleExcelWriter implements ExcelWriter {

    private final ConverterFactory converterFactory;
    private final WorkbookSheetWriteConfiguration configuration;

    public SimpleExcelWriter(ConverterFactory converterFactory, WorkbookSheetWriteConfiguration configuration) {
        this.converterFactory = converterFactory;
        this.configuration = configuration;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    @Override
    public <T> ExcelSheetConfigurer<T> type(Class<T> type) {
        return new SimpleExcelSheetConfigurer<>(TypeDataExtractor.INSTANCE, type, this.getConverterFactory(),this.getConfiguration());
    }

    @Override
    public <T> AnnotationExcelSheetConfigurer<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new AnnotationExcelSheetConfigurer<>(new AnnotationDataExtractor(), type, this.getConverterFactory(),this.getConfiguration());
    }

    @Override
    public <T> ExcelSheetConfigurer<T> custom(DataExtractor<?> dataExtractor, Class<T> type) {
        return new SimpleExcelSheetConfigurer<>(dataExtractor, type, this.getConverterFactory(), this.getConfiguration());
    }

    public WorkbookSheetWriteConfiguration getConfiguration() {
        return this.configuration;
    }

}
