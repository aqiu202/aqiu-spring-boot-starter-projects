package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.ValueDescriptor;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;

public class FieldValueDescriptor implements ValueDescriptor {

    private final Field field;

    public FieldValueDescriptor(Field field) {
        this.field = field;
    }

    @Override
    public Class<?> getValueType() {
        return this.field.getType();
    }

    @Override
    public Object getValue(Object instance) {
        return ReflectionUtils.getValue(instance, this.field);
    }

    @Override
    public void setValue(Object instance, Object value) {
        ReflectionUtils.setValue(instance, this.field, value);
    }
}
