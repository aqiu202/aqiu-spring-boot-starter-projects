package com.github.aqiu202.excel.model;

import com.github.aqiu202.excel.extract.AnnotationDataExtractor;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.TypeDataExtractor;

public enum DataExtractMode {

    FIELD(new TypeDataExtractor()),
    ANNOTATION(new AnnotationDataExtractor());

    private final DataExtractor<?> dataExtractor;

    DataExtractMode(DataExtractor<?> dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    public DataExtractor<?> getDataExtractor() {
        return dataExtractor;
    }

}
