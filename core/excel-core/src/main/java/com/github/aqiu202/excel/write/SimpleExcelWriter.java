package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public class SimpleExcelWriter extends SimpleConfigurableExcelWriter<ExcelWriter> implements ExcelWriter {

    private final ConverterFactory converterFactory;

    public SimpleExcelWriter(SheetWriteConfiguration configuration, ConverterFactory converterFactory) {
        super.configuration = configuration;
        this.converterFactory = converterFactory;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    @Override
    public <T> ItemExcelWriter<T> type(Class<T> type) {
        return new SimpleItemExcelWriter<>(
                new SimpleTypedExcelWriter(this.getConfiguration(), this.getConverterFactory()), type);
    }

    @Override
    public <T> ItemExcelWriter<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new SimpleItemExcelWriter<>(
                new SimpleAnnotationExcelWriter(this.getConfiguration(), this.getConverterFactory()), type);
    }

    @Override
    public SheetExcelWriter sheetName(String sheetName) {
        return new SimpleSheetExcelWriter(sheetName, this.getConfiguration(), this.getConverterFactory());
    }

    @Override
    public BatchExcelWriter batch() {
        return new SimpleBatchExcelWriter(this.getConfiguration(), this.getConverterFactory());
    }
}
