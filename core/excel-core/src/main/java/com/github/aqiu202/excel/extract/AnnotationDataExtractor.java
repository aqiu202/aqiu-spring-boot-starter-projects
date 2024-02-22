package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.analy.AnnotationTypeAnalyzer;
import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.meta.ExcelColumnField;
import com.github.aqiu202.util.ReflectionUtils;
import com.github.aqiu202.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class AnnotationDataExtractor extends AbstractDataExtractor<ExcelColumnField> {

    private final AnnotationTypeAnalyzer annotationTypeAnalyzer = new AnnotationTypeAnalyzer();

    public void setWriteNonAnnotationFields(boolean writeNonAnnotationFields) {
        this.annotationTypeAnalyzer.setWriteNonAnnotationFields(writeNonAnnotationFields);
    }

    @Override
    public List<ExcelColumnField> analysisMetas(Class<?> type) {
        return this.annotationTypeAnalyzer.analysis(type);
    }

    @Override
    public Object extractValue(ExcelColumnField annotatedField, Object instance) {
        Field field = annotatedField.getField();
        Object result = ReflectionUtils.getValue(instance, field);
        if (annotatedField.hasAnnotation()) {
            ExcelColumn annotation = annotatedField.getAnnotation();
            String fieldName = annotation.field();
            if (StringUtils.isNotBlank(fieldName)) {
                String[] fs = fieldName.split("\\.");
                if (result != null && fs.length > 1) {
                    String[] properties = Arrays.copyOfRange(fs, 1, fs.length);
                    result = ReflectionUtils.getValue(result, properties);
                }
            }
        }
        return result;
    }
}
