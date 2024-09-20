package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.StringUtils;

import java.util.Map;

public class MapBeanProperty implements BeanProperty {

    private final String propertyName;

    public MapBeanProperty(String propertyName) {
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
        if (instance instanceof Map) {
            ((Map) instance).put(this.getPropertyName(), value);
        }
    }

    @Override
    public Object getValue(Object instance) {
        if (instance instanceof Map) {
            return ((Map) instance).get(this.getPropertyName());
        }
        return null;
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(this.getPropertyName());
    }
}
