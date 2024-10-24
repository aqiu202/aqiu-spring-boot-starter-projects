package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.prop.FieldValueDescriptor;

import java.lang.reflect.Field;

public class FieldMeta implements DataMeta {

    private final Field field;

    public FieldMeta(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }


    @Override
    public String getKey() {
        return this.field.getName();
    }

    @Override
    public ValueDescriptor getValueDescriptor() {
        return new FieldValueDescriptor(this.field);
    }

    @Override
    public HeadDescriptor getHeadDescriptor() {
        String[] contents = {this.field.getName()};
        return new SimpleHeadDescriptor(contents);
    }
}
