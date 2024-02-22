package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.meta.FieldProperty;
import com.github.aqiu202.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PropertyDataExtractor extends AbstractDataExtractor<FieldProperty> {

    private final List<FieldProperty> fieldProperties;

    public PropertyDataExtractor(Collection<FieldProperty> fieldProperties) {
        this.fieldProperties = new ArrayList<>(fieldProperties);
    }

    @Override
    public List<FieldProperty> analysisMetas(Class<?> type) {
        return this.fieldProperties;
    }

    @Override
    public Object extractValue(FieldProperty property, Object instance) {
        String fieldName = property.getFieldName();
        if (instance instanceof Map) {
            return ((Map<?, ?>) instance).get(fieldName);
        }
        return ReflectionUtils.getComplexValue(instance, fieldName);
    }
}
