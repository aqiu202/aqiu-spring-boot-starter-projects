package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.meta.FieldProperty;

import java.util.Arrays;
import java.util.List;

public class PropertyValueMapping extends AbstractValueMapping<FieldProperty> {

    private final List<FieldProperty> fieldProperties;

    public PropertyValueMapping(List<FieldProperty> fieldProperties) {
        this.fieldProperties = fieldProperties;
    }

    public PropertyValueMapping(FieldProperty... fieldProperties) {
        this(Arrays.asList(fieldProperties));
    }

    @Override
    public List<FieldProperty> analysisMetas(Class<?> type) {
        return this.fieldProperties;
    }

}
