package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.meta.PropertyTableMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyDataExtractor extends AbstractDataExtractor<PropertyTableMeta> {

    private final List<PropertyTableMeta> fieldProperties;

    public PropertyDataExtractor(Collection<PropertyTableMeta> fieldProperties) {
        this.fieldProperties = new ArrayList<>(fieldProperties);
    }

    @Override
    public MetaAnalyzer<PropertyTableMeta> getMetaAnalyzer() {
        return null;
    }

    @Override
    public List<PropertyTableMeta> extractMetas(Class<?> dataType) {
        return this.fieldProperties;
    }

}
