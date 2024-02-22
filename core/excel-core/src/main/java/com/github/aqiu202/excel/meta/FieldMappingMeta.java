package com.github.aqiu202.excel.meta;

import java.lang.reflect.Field;

public class FieldMappingMeta implements MappingMeta {

    private final Field field;

    public FieldMappingMeta(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String getFieldName() {
        return this.field.getName();
    }

    @Override
    public String getFieldTitle() {
        return this.getFieldName();
    }
}
