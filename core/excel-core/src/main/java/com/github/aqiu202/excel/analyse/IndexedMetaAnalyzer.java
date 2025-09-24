package com.github.aqiu202.excel.analyse;

import com.github.aqiu202.excel.meta.IndexedTableMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexedMetaAnalyzer implements MetaAnalyzer<IndexedTableMeta> {

    private final List<IndexedTableMeta> metas;

    public IndexedMetaAnalyzer(String... properties) {
        this(Arrays.asList(properties));
    }

    public IndexedMetaAnalyzer(List<String> properties) {
        List<IndexedTableMeta> metas = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            String property = properties.get(i);
            metas.add(new IndexedTableMeta(i, property, property));
        }
        this.metas = metas;
    }

    @Override
    public List<IndexedTableMeta> analyseMetas(Class<?> type) {
        return this.metas;
    }

}
