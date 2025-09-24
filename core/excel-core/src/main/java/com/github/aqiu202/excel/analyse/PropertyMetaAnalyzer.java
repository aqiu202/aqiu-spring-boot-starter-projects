package com.github.aqiu202.excel.analyse;

import com.github.aqiu202.excel.meta.BeanPropertyMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyMetaAnalyzer implements MetaAnalyzer<BeanPropertyMeta> {

    private final List<BeanProperty> propertyMetas;

    public PropertyMetaAnalyzer(BeanProperty... propertyMetas) {
        this(Arrays.asList(propertyMetas));
    }

    public PropertyMetaAnalyzer(List<BeanProperty> propertyMetas) {
        this.propertyMetas = propertyMetas;
    }

    @Override
    public List<BeanPropertyMeta> analyseMetas(Class<?> type) {
        return this.propertyMetas.stream()
                .map(mapMeta -> new BeanPropertyMeta(type, mapMeta))
                .collect(Collectors.toList());
    }

}
