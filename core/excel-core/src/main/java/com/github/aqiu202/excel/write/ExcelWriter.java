package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.write.extract.DataExtractor;

public interface ExcelWriter {
    /**
     * 基于反射读取元数据
     */
    <T> ExcelSheetConfigurer<T> type(Class<T> type);

    /**
     * 基于注解读取元数据
     */
    <T> AnnotationExcelSheetConfigurer<T> annotation(Class<T> type);

    <T> ExcelSheetConfigurer<T> custom(DataExtractor<?> dataExtractor, Class<T> type);

}
