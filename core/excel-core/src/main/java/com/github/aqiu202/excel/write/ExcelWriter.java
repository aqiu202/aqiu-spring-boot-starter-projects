package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.extract.IndexedDataExtractor;
import com.github.aqiu202.excel.write.extract.PropertyDataExtractor;

import java.util.List;

public interface ExcelWriter {
    /**
     * 基于反射读取元数据
     */
    <T> ExcelSheetConfigurer<T> type(Class<T> type);

    /**
     * 基于注解读取元数据
     */
    <T> AnnotationExcelSheetConfigurer<T> annotation(Class<T> type);

    default ExcelSheetConfigurer indexedProperties(List<String> fieldProperties) {
        return this.indexedProperties(fieldProperties, null);
    }

    default <T> ExcelSheetConfigurer<T> indexedProperties(List<String> fieldProperties, Class<T> type) {
        return this.custom(new IndexedDataExtractor(fieldProperties), type);
    }

    default ExcelSheetConfigurer properties(PropertyDataExtractor dataExtractor) {
        return this.properties(dataExtractor, null);
    }

    default <T> ExcelSheetConfigurer<T> properties(PropertyDataExtractor dataExtractor, Class<T> type) {
        return this.custom(dataExtractor, type);
    }

    <T> ExcelSheetConfigurer<T> custom(DataExtractor<?> dataExtractor, Class<T> type);

}
