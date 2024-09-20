package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.FieldMetaAnalyzer;
import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.meta.FieldMeta;

public class TypeDataExtractor extends AbstractDataExtractor<FieldMeta> {

    public static final TypeDataExtractor INSTANCE = new TypeDataExtractor();

    private final FieldMetaAnalyzer metaAnalyzer = new FieldMetaAnalyzer();

    @Override
    public MetaAnalyzer<FieldMeta> getMetaAnalyzer() {
        return this.metaAnalyzer;
    }


}
