package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PropertyField {

    private final Field field;
    private final String propertyName;

    private final Class<?> beanType;

    public PropertyField(Class<?> beanType, String propertyName) {
        this.beanType = beanType;
        this.field = ReflectionUtils.getField(beanType, propertyName);
        this.propertyName = propertyName;
    }

    public Field getField() {
        return field;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
