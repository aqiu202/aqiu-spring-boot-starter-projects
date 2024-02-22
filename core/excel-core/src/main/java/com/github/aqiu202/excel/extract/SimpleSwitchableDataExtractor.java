package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.DataExtractMode;

public class SimpleSwitchableDataExtractor<T extends MappingMeta> implements SwitchableDataExtractor<T> {

    private DataExtractMode extractMode = DataExtractMode.FIELD;

    public SimpleSwitchableDataExtractor() {
    }

    public SimpleSwitchableDataExtractor(DataExtractMode extractMode) {
        this.extractMode = extractMode;
    }

    @Override
    public SimpleSwitchableDataExtractor<T> extractMode(DataExtractMode extractMode) {
        if (extractMode != null) {
            this.extractMode = extractMode;
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataExtractor<T> getCurrentDataExtractor() {
        return (DataExtractor<T>) this.extractMode.getDataExtractor();
    }


}
