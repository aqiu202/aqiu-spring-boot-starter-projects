package com.github.aqiu202.excel.extract;

public interface DataExtractorWrapper<T extends DataExtractorWrapper<?>> {
    T dataExtractor(DataExtractor<?> dataExtractor);

    DataExtractor<?> getDataExtractor();

}
