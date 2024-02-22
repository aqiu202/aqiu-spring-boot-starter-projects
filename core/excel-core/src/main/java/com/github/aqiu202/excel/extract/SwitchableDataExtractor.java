package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.wrap.ResultWrapper;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.DataExtractMode;

import java.util.List;

public interface SwitchableDataExtractor<T extends MappingMeta> extends DataExtractor<T> {

    SwitchableDataExtractor<T> extractMode(DataExtractMode extractMode);

    DataExtractor<T> getCurrentDataExtractor();

    @Override
    default List<T> analysisMetas(Class<?> type) {
        return this.getCurrentDataExtractor().analysisMetas(type);
    }

    @Override
    default FormatterProvider extractFormatterProvider(MappingMeta meta, FormatterProvider global) {
        return this.getCurrentDataExtractor().extractFormatterProvider(meta, global);
    }

    @Override
    default Object extractValue(T meta, Object instance) {
        return this.getCurrentDataExtractor().extractValue(meta, instance);
    }

    @Override
    default ResultWrapper<?> extractFormattedValue(T meta, Object instance, FormatterProvider global, ConverterFactory converterFactory) {
        return this.getCurrentDataExtractor().extractFormattedValue(meta, instance, global, converterFactory);
    }
}
