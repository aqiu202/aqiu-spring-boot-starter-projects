package com.github.aqiu202.excel.analyse;

import com.github.aqiu202.excel.meta.BeanPropertyMeta;

import java.util.Collections;
import java.util.List;

public class PropertyMetaAnalyzer implements MetaAnalyzer<BeanPropertyMeta> {
    @Override
    public List<BeanPropertyMeta> analyseMetas(Class<?> type) {
        return Collections.emptyList();
    }
}
