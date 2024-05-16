package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.analyse.AnnotationMetaAnalyzer;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.write.extract.AnnotationDataExtractor;

public class AnnotationExcelSheetConfigurer<T> extends SimpleExcelSheetConfigurer<T> {

    private final AnnotationMetaAnalyzer metaAnalyzer;

    public AnnotationExcelSheetConfigurer(AnnotationDataExtractor dataExtractor, Class<T> dataType, ExcelSheetWriter<?> sheetWriter) {
        super(dataExtractor, dataType, sheetWriter);
        this.metaAnalyzer = dataExtractor.getMetaAnalyzer();
    }

    public AnnotationExcelSheetConfigurer(AnnotationDataExtractor dataExtractor, Class<T> dataType, ConverterFactory converterFactory, WorkbookSheetWriteConfiguration configuration) {
        super(dataExtractor, dataType, converterFactory, configuration);
        this.metaAnalyzer = dataExtractor.getMetaAnalyzer();
    }

    public AnnotationExcelSheetConfigurer<T> strict(boolean strict) {
        this.metaAnalyzer.setStrict(strict);
        return this;
    }

    public AnnotationExcelSheetConfigurer<T> propertyAccessor(PropertyAccessor propertyAccessor) {
        this.metaAnalyzer.setPropertyAccessor(propertyAccessor);
        return this;
    }
}
