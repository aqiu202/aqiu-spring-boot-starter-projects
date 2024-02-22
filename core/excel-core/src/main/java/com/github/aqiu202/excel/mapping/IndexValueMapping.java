package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.meta.IndexMappingMeta;

import java.util.List;

public class IndexValueMapping extends AbstractValueMapping<IndexMappingMeta> {

    private final List<IndexMappingMeta> fieldProperties;

    public IndexValueMapping(String... fieldNames) {
        this(0, fieldNames);
    }

    public IndexValueMapping(List<String> fieldNames) {
        this(0, fieldNames);
    }

    public IndexValueMapping(int startColumnNum, String... fieldProperties) {
        this.fieldProperties = IndexMappingMeta.generate(startColumnNum, fieldProperties);
    }

    public IndexValueMapping(int startColumnNum, List<String> fieldProperties) {
        this.fieldProperties = IndexMappingMeta.generate(startColumnNum, fieldProperties);
    }

    @Override
    public List<IndexMappingMeta> analysisMetas(Class<?> type) {
        return this.fieldProperties;
    }

}
