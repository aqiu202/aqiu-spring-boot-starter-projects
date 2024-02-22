package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.analy.FieldTypeAnalyzer;
import com.github.aqiu202.excel.analy.TypeAnalyzer;
import com.github.aqiu202.excel.meta.FieldMappingMeta;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class TypeDataExtractor extends AbstractDataExtractor<FieldMappingMeta> {

    private final TypeAnalyzer<Field> typeAnalyzer = new FieldTypeAnalyzer();

    @Override
    public List<FieldMappingMeta> analysisMetas(Class<?> type) {
        return this.typeAnalyzer.analysis(type).stream().map(FieldMappingMeta::new).collect(Collectors.toList());
    }

    @Override
    public Object extractValue(FieldMappingMeta meta, Object instance) {
        Field field = meta.getField();
        return ReflectionUtils.getValue(instance, field);
    }
}
