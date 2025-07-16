package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.convert.ConverterProvider;
import com.github.aqiu202.excel.convert.ConverterProviderWrapper;
import com.github.aqiu202.excel.format.AnnotationFormatterProvider;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.model.AnnotatedField;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.prop.ProxyBeanValueDescriptor;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;

import java.lang.reflect.Field;

public class ExcelFieldMeta extends AnnotatedField<ExcelColumn> implements DataMeta, ConverterProviderWrapper,
        FormatterProviderWrapper {

    private AnnotationFormatterProvider provider;
    private String formula;
    private boolean image;
    private int width = 0;
    private final PropertyAccessor propertyAccessor;

    public ExcelFieldMeta(Field field, PropertyAccessor propertyAccessor) {
        super(field);
        this.propertyAccessor = propertyAccessor;
    }

    public ExcelFieldMeta(Field field, ExcelColumn annotation, PropertyAccessor propertyAccessor) {
        super(field, annotation);
        this.propertyAccessor = propertyAccessor;
        if (annotation != null) {
            this.provider = new AnnotationFormatterProvider(annotation);
            this.formula = annotation.formula();
            this.image = annotation.image();
            this.width = annotation.width();
        }
    }

    @Override
    public String getKey() {
        String fieldPath = null;
        if (this.hasAnnotation()) {
            ExcelColumn anno = this.getAnnotation();
            String fieldName = anno.field();
            if (StringUtils.isNotBlank(fieldName)) {
                fieldPath = fieldName;
            }
        }
        if (StringUtils.isBlank(fieldPath)) {
            fieldPath = this.getField().getName();
        }
        return fieldPath;
    }

    @Override
    public String getFormula() {
        return this.formula;
    }

    @Override
    public boolean isImage() {
        return this.image;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public ValueDescriptor getValueDescriptor() {
        PropertyAccessor accessor = this.propertyAccessor;
        if (this.hasAnnotation()) {
            ExcelColumn anno = this.getAnnotation();
            String accessorName = anno.accessor();
            if (StringUtils.equalsAnyIgnoreCase(accessorName, "METHOD", "FIELD")) {
                accessor = PropertyAccessor.parse(accessorName);
            }
        }
        if (accessor == null) {
            accessor = PropertyAccessor.FIELD;
        }
        return new ProxyBeanValueDescriptor(this.getField().getDeclaringClass(), this.getKey(), accessor);
    }

    @Override
    public HeadDescriptor getHeadDescriptor() {
        String[] contents = null;
        if (this.hasAnnotation()) {
            contents = this.getAnnotation().value();
        }
        if (CollectionUtils.isEmpty(contents)) {
            contents = new String[]{this.getField().getName()};
        }
        return new SimpleHeadDescriptor(contents);
    }

    @Override
    public FormatterProvider getProvider() {
        return this.provider;
    }

    @Override
    public ConverterProvider getConverterProvider() {
        return this.hasAnnotation() ? this.provider : null;
    }

    @Override
    public int getOrder() {
        return this.hasAnnotation() ? this.getAnnotation().order() : DataMeta.super.getOrder();
    }
}
