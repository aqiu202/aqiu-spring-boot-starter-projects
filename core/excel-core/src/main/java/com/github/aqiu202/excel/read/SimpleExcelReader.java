package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.AnnotationMetaAnalyzer;
import com.github.aqiu202.excel.analyse.FieldMetaAnalyzer;
import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetReadConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleExcelReader implements ExcelReader {

    private final ConverterFactory converterFactory;
    private final SheetReadConfiguration configuration;

    public SimpleExcelReader(ConverterFactory converterFactory, SheetReadConfiguration configuration) {
        this.converterFactory = converterFactory;
        this.configuration = configuration;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public SheetReadConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> ExcelSheetReader<T> type(Class<T> type) {
        return this.custom(type, FieldMetaAnalyzer.INSTANCE);
    }

    @Override
    public <T> AnnotationExcelSheetReader<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new AnnotationExcelSheetReader<>(type, new AnnotationMetaAnalyzer(), this.getConverterFactory(), this.getConfiguration());
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ExcelSheetReader<Map<String, Object>> map() {
        return (ExcelSheetReader) this.type(LinkedHashMap.class);
    }

    @Override
    public <T> ExcelSheetReader<T> custom(Class<T> type, MetaAnalyzer<?> metaAnalyzer) {
        return new SimpleExcelSheetReader<>(type, metaAnalyzer, this.getConverterFactory(), this.getConfiguration());
    }
}
