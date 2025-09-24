package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.ReflectionUtils;
import com.github.aqiu202.util.StringUtils;

import java.util.Map;

public class MapValueDescriptor implements BeanValueDescriptor {

    private final String propertyName;

    public MapValueDescriptor(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Class<?> getBeanType() {
        return Map.class;
    }

    @Override
    public Class<?> getValueType() {
        return null;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public void setValue(Object instance, Object value) {
        String propertyName = this.getPropertyName();
        if (instance instanceof Map) {
            ((Map) instance).put(propertyName, value);
        } else {
            ReflectionUtils.setValue(instance, propertyName, value);
        }
    }

    @Override
    public Object getValue(Object instance) {
        String propertyName = this.getPropertyName();
        if (instance instanceof Map) {
            return ((Map) instance).get(propertyName);
        }
        return ReflectionUtils.getValue(instance, propertyName);
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(this.getPropertyName());
    }
}
