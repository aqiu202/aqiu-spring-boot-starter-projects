package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.TypeDataExtractor;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public class SimpleTypedExcelWriter extends SimpleCombineExcelWriter<TypedExcelWriter>
        implements TypedExcelWriter {


    public SimpleTypedExcelWriter(SheetWriteConfiguration configuration,
                                  ConverterFactory converterFactory) {
        this(configuration, new TypeDataExtractor(), converterFactory);
    }

    protected SimpleTypedExcelWriter(SheetWriteConfiguration configuration,
                                     DataExtractor<?> dataExtractor, ConverterFactory converterFactory) {
        super(dataExtractor, converterFactory);
        super.configuration(configuration);
    }

    @Override
    public <S> ItemExcelWriter<S> append(Class<S> type) {
        return new SimpleItemExcelWriter<>(this, type);
    }

}
