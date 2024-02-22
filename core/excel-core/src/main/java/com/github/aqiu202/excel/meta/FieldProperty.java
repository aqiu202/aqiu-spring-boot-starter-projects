package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.convert.ConverterProvider;
import com.github.aqiu202.excel.convert.ConverterProviderWrapper;
import com.github.aqiu202.excel.format.*;

public class FieldProperty implements MappingMeta, ConverterProviderWrapper, FormatterProviderWrapper {

    private String fieldName;
    private String fieldTitle;

    private ConverterProvider converterProvider;

    private FormatterProvider formatterProvider;

    @Override
    public ConverterProvider getConverterProvider() {
        return converterProvider;
    }

    public void setConverterProvider(ConverterProvider converterProvider) {
        this.converterProvider = converterProvider;
    }

    @Override
    public FormatterProvider getFormatterProvider() {
        return formatterProvider;
    }

    public void setFormatterProvider(FormatterProvider formatterProvider) {
        this.formatterProvider = formatterProvider;
    }

    public FieldProperty() {
    }

    public FieldProperty(String fieldName) {
        this(fieldName, null);
    }

    public FieldProperty(String fieldName, String fieldTitle) {
        this.fieldName = fieldName;
        this.fieldTitle = fieldTitle;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public FieldProperty setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public String getFieldTitle() {
        return fieldTitle;
    }

    public FieldProperty setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
        return this;
    }
}
