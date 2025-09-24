package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.IndexedMetaAnalyzer;
import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.read.cell.HeadMeta;

import java.util.List;

public class IndexedDataAnalyser extends SimpleDataAnalyser {

    public IndexedDataAnalyser(String... properties) {
        super(new IndexedMetaAnalyzer(properties));
    }

    public IndexedDataAnalyser(List<String> properties) {
        super(new IndexedMetaAnalyzer(properties));
    }

    public IndexedDataAnalyser(IndexedMetaAnalyzer metaAnalyzer) {
        super(metaAnalyzer);
    }

    @Override
    protected List<IndexedTableMeta> resolveIndexedTableMeta(Class<?> type, List<? extends TableMeta> metas, HeadMeta[] headMetas) {
        return (List<IndexedTableMeta>) metas;
    }
}
