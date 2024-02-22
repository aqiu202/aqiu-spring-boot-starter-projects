package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.convert.ConverterProvider;
import com.github.aqiu202.excel.convert.ConverterProviderWrapper;
import com.github.aqiu202.excel.format.AnnotationFormatterProvider;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.model.AnnotatedField;
import com.github.aqiu202.util.StringUtils;

import java.lang.reflect.Field;

public class ExcelColumnField extends AnnotatedField<ExcelColumn> implements MappingMeta, ConverterProviderWrapper,
        FormatterProviderWrapper {

    private ConverterProvider converterProvider;

    private FormatterProvider formatterProvider;

    public ExcelColumnField(Field field) {
        super(field);
    }

    public ExcelColumnField(Field field, ExcelColumn annotation) {
        super(field, annotation);
        if (annotation != null) {
            this.formatterProvider = new AnnotationFormatterProvider(annotation);
            String converter = annotation.converter();
            if (StringUtils.isNotBlank(converter)) {
                this.converterProvider = () -> converter;
            }
        }
    }

    @Override
    public String getFieldName() {
        if (this.hasAnnotation()) {
            String fieldName = this.getAnnotation().field();
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
            }
        }
        return this.getField().getName();
    }

    @Override
    public String getFieldTitle() {
        if (this.hasAnnotation()) {
            String title = this.getAnnotation().value();
            if (StringUtils.isNotBlank(title)) {
                return title;
            }
        }
        return this.getField().getName();
    }

    @Override
    public FormatterProvider getFormatterProvider() {
        return this.formatterProvider;
    }

    @Override
    public ConverterProvider getConverterProvider() {
        return this.converterProvider;
    }
}
