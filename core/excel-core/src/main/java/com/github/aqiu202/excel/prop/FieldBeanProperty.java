package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;

public class FieldBeanProperty implements BeanProperty {

    private final Class<?> beanType;
    private final String propertyName;
    private final PathElements<PropertyField> pathFields;

    public FieldBeanProperty(Class<?> beanType, String propertyName) {
        this.beanType = beanType;
        this.propertyName = propertyName;
        this.pathFields = new SimplePathFields(beanType, propertyName);
    }

    @Override
    public Class<?> getBeanType() {
        return this.beanType;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public void setValue(Object instance, Object value) {
        Object result = instance;
        PropertyField[] fields = this.pathFields.getPreElements();
        if (fields != null) {
            for (PropertyField propertyField : fields) {
                if (propertyField == null) {
                    return;
                }
                Class<?> type = this.pathFields.getType(propertyField);
                if (ClassUtils.isCollection(type) || ClassUtils.isMap(type)) {
                    return;
                }
                Field field = propertyField.getField();
                Object child = ReflectionUtils.getValue(result, field);
                if (child == null) {
                    child = ClassUtils.newInstance(type);
                    ReflectionUtils.setValue(result, field, child);
                }
                result = child;
            }
        }
        PropertyField propertyField = pathFields.getLastElement();
        if (propertyField != null) {
            ReflectionUtils.setValue(result, propertyField.getField(), value);
        }
    }

    @Override
    public Object getValue(Object instance) {
        if (this.pathFields.isEmpty()) {
            return null;
        }
        PropertyField[] propertyFields = this.pathFields.getContent();
        Object result = instance;
        for (PropertyField propertyField : propertyFields) {
            if (propertyField == null) {
                return null;
            }
            Class<?> type = this.pathFields.getType(propertyField);
            if (ClassUtils.isCollection(type) || ClassUtils.isMap(type)) {
                return null;
            }
            result = ReflectionUtils.getValue(result, propertyField.getField());
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Class<?> getPropertyType() {
        return this.pathFields.getLastElementType();
    }

    @Override
    public boolean isValid() {
        return !this.pathFields.isEmpty();
    }
}
