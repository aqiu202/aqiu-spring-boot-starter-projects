package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.meta.MapPropertyMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyDataExtractor extends AbstractDataExtractor<MapPropertyMeta> {

    private final List<MapPropertyMeta> fieldProperties;

    public PropertyDataExtractor(Collection<MapPropertyMeta> fieldProperties) {
        this.fieldProperties = new ArrayList<>(fieldProperties);
    }

    @Override
    public MetaAnalyzer<MapPropertyMeta> getMetaAnalyzer() {
        return null;
    }

    @Override
    public List<MapPropertyMeta> extractMetas(Class<?> dataType) {
        return this.fieldProperties;
    }

}
