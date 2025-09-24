package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.meta.IndexedTableMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IndexedDataExtractor extends AbstractDataExtractor<IndexedTableMeta> {

    private final List<IndexedTableMeta> fieldProperties;

    public IndexedDataExtractor(String... fieldProperties) {
        this(Arrays.asList(fieldProperties));
    }

    public IndexedDataExtractor(List<String> fieldProperties) {
        List<IndexedTableMeta> metas = new ArrayList<>();
        for (int i = 0; i < fieldProperties.size(); i++) {
            String property = fieldProperties.get(i);
            metas.add(new IndexedTableMeta(i, property, property));
        }
        this.fieldProperties = metas;
    }

    public IndexedDataExtractor(Collection<IndexedTableMeta> fieldProperties) {
        this.fieldProperties = new ArrayList<>(fieldProperties);
    }

    @Override
    public MetaAnalyzer<IndexedTableMeta> getMetaAnalyzer() {
        return null;
    }

    @Override
    public List<IndexedTableMeta> extractMetas(Class<?> dataType) {
        return this.fieldProperties;
    }

}
