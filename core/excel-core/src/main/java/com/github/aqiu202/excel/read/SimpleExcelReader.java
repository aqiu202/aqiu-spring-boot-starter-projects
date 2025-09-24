package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.AnnotationMetaAnalyzer;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetReadConfiguration;

public class SimpleExcelReader implements ExcelReader {

    private final ConverterFactory converterFactory;
    private final SheetReadConfiguration configuration;

    private final ExcelBeforeReadHandler beforeReadHandler;

    public SimpleExcelReader(ConverterFactory converterFactory, SheetReadConfiguration configuration, ExcelBeforeReadHandler beforeReadHandler) {
        this.converterFactory = converterFactory;
        this.configuration = configuration;
        this.beforeReadHandler = beforeReadHandler;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public SheetReadConfiguration getConfiguration() {
        return configuration;
    }

    public ExcelBeforeReadHandler getBeforeReadHandler() {
        return beforeReadHandler;
    }

    @Override
    public <T> AnnotationExcelSheetReader<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new AnnotationExcelSheetReader<>(type, new AnnotationMetaAnalyzer(), this.getConverterFactory(),
                this.getConfiguration(), this.getBeforeReadHandler());
    }

    @Override
    public <T> ExcelSheetReader<T> custom(Class<T> type, DataAnalyser dataAnalyser) {
        return new SimpleExcelSheetReader<>(type, dataAnalyser, this.getConverterFactory(), this.getConfiguration(), this.getBeforeReadHandler());
    }

    @Override
    public RowExcelSheetReader rows() {
        return new RowExcelSheetReader(this.getConverterFactory(), this.getConfiguration(), this.getBeforeReadHandler());
    }
}
