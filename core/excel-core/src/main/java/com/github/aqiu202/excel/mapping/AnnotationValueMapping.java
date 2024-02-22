package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.analy.AnnotationTypeAnalyzer;
import com.github.aqiu202.excel.meta.ExcelColumnField;

import java.util.List;

public class AnnotationValueMapping extends AbstractValueMapping<ExcelColumnField> {

    private final AnnotationTypeAnalyzer annotationTypeAnalyzer = new AnnotationTypeAnalyzer();

    public void setWriteNonAnnotationFields(boolean writeNonAnnotationFields) {
        this.annotationTypeAnalyzer.setWriteNonAnnotationFields(writeNonAnnotationFields);
    }

    @Override
    public List<ExcelColumnField> analysisMetas(Class<?> type) {
        return annotationTypeAnalyzer.analysis(type);
    }

}
