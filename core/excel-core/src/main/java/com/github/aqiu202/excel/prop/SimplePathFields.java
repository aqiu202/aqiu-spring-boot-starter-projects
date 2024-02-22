package com.github.aqiu202.excel.prop;

import java.lang.reflect.Field;

public class SimplePathFields extends AbstractPathElements<PropertyField> {


    public SimplePathFields(Class<?> type, String propertyName) {
        super(type, propertyName);
    }

    @Override
    public PropertyField createElement(Class<?> beanType, String propertyName) {
        try {
            return new PropertyField(beanType, propertyName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PropertyField[] createEmptyElementArray(int length) {
        return new PropertyField[length];
    }

    @Override
    public Class<?> getType(PropertyField element) {
        Field field = element.getField();
        if (field == null) {
            return null;
        }
        return field.getType();
    }
}
