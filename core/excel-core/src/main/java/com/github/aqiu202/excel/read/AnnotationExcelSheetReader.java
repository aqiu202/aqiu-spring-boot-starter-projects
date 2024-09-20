package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.AnnotationMetaAnalyzer;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.model.ReadConfiguration;

public class AnnotationExcelSheetReader<T> extends SimpleExcelSheetReader<T> {

    private final AnnotationMetaAnalyzer metaAnalyzer;

    public AnnotationExcelSheetReader(Class<T> type, AnnotationMetaAnalyzer metaAnalyzer, ConverterFactory converterFactory, ReadConfiguration configuration) {
        super(type, metaAnalyzer, converterFactory, configuration);
        this.metaAnalyzer = metaAnalyzer;
    }

    public AnnotationExcelSheetReader<T> strict(boolean strict) {
        this.metaAnalyzer.setStrict(strict);
        return this;
    }

    public AnnotationExcelSheetReader<T> propertyAccessor(PropertyAccessor propertyAccessor) {
        this.metaAnalyzer.setPropertyAccessor(propertyAccessor);
        return this;
    }
}
