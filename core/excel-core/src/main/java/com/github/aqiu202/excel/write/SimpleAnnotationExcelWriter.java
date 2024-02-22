package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.AnnotationDataExtractor;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public class SimpleAnnotationExcelWriter extends SimpleTypedExcelWriter implements AnnotationExcelWriter {

    public SimpleAnnotationExcelWriter(SheetWriteConfiguration configuration, ConverterFactory converterFactory) {
        super(configuration, new AnnotationDataExtractor(), converterFactory);
    }

    @Override
    public AnnotationExcelWriter setWriteNonAnnotationFields(boolean writeNonAnnotationFields) {
        DataExtractor<?> dataExtractor = this.getDataExtractor();
        if (dataExtractor instanceof AnnotationDataExtractor) {
            ((AnnotationDataExtractor) dataExtractor).setWriteNonAnnotationFields(writeNonAnnotationFields);
        }
        return this;
    }

}
