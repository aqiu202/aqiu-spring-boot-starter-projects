package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class MethodBeanValueDescriptor implements BeanValueDescriptor {

    private final Class<?> beanType;
    private final String propertyName;
    private final PathElements<PropertyDescriptor> descriptors;

    public MethodBeanValueDescriptor(Class<?> beanType, String propertyName) {
        this.beanType = beanType;
        this.propertyName = propertyName;
        this.descriptors = new SimplePathDescriptors(beanType, propertyName);
    }

    @Override
    public Class<?> getValueType() {
        return this.descriptors.getLastElementType();
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
        PropertyDescriptor[] descriptors = this.descriptors.getPreElements();
        Object result = instance;
        if (descriptors != null) {
            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor == null) {
                    return;
                }
                Class<?> type = this.descriptors.getType(descriptor);
                if (ClassUtils.isCollection(type) || ClassUtils.isMap(type)) {
                    return;
                }
                Object child;
                try {
                    child = descriptor.getReadMethod().invoke(instance);
                    if (child == null) {
                        child = ClassUtils.newInstance(type);
                        descriptor.getWriteMethod().invoke(instance, child);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return;
                }
                result = child;
            }
        }
        PropertyDescriptor descriptor = this.descriptors.getLastElement();
        if (descriptor != null) {
            try {
                descriptor.getWriteMethod().invoke(result, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        }
    }

    @Override
    public Object getValue(Object instance) {
        if (this.descriptors.isEmpty()) {
            return null;
        }
        PropertyDescriptor[] descriptors = this.descriptors.getContent();
        Object result = instance;
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor == null) {
                return null;
            }
            Class<?> type = this.descriptors.getType(descriptor);
            if (ClassUtils.isCollection(type) || ClassUtils.isMap(type)) {
                return null;
            }
            try {
                result = descriptor.getReadMethod().invoke(result);
                if (result == null) {
                    return null;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
        return result;
    }

    @Override
    public boolean isValid() {
        return !this.descriptors.isEmpty();
    }
}
