package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.analy.FieldTypeAnalyzer;
import com.github.aqiu202.excel.analy.TypeAnalyzer;
import com.github.aqiu202.excel.meta.FieldMappingMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class FieldValueMapping extends AbstractValueMapping<FieldMappingMeta> {

    private final TypeAnalyzer<Field> typeAnalyzer = new FieldTypeAnalyzer();

    @Override
    public List<FieldMappingMeta> analysisMetas(Class<?> type) {
        return this.typeAnalyzer.analysis(type).stream().map(FieldMappingMeta::new).collect(Collectors.toList());
    }

}
