package com.github.aqiu202.excel.analy;


import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class FieldTypeAnalyzer implements TypeAnalyzer<Field>{

    @Override
    public List<Field> analysis(Class<?> type) {
        return this.parseFields(type);
    }

    private List<Field> parseFields(Class<?> type) {
        return ReflectionUtils.getAllField(type).stream()
                .filter(field -> this.isBasicType(field.getType()))
                .collect(Collectors.toList());
    }

    private boolean isBasicType(Class<?> type) {
        return ClassUtils.isBasicType(type)
                || ClassUtils.isNumber(type)
                || ClassUtils.isDate(type)
                || String.class.equals(type);
    }
}
