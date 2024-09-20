package com.github.aqiu202.excel.prop;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public class SimplePathDescriptors extends AbstractPathElements<PropertyDescriptor> {


    public SimplePathDescriptors(Class<?> type, String propertyName) {
        super(type, propertyName);
    }

    @Override
    public PropertyDescriptor createElement(Class<?> beanType, String propertyName) {
        try {
            return new PropertyDescriptor(propertyName, beanType);
        } catch (IntrospectionException e) {
            return null;
        }
    }

    @Override
    public PropertyDescriptor[] createEmptyElementArray(int length) {
        return new PropertyDescriptor[length];
    }

    @Override
    public Class<?> getType(PropertyDescriptor element) {
        return element.getPropertyType();
    }
}
