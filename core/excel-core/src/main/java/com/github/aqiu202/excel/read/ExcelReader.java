package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;

import java.util.HashMap;
import java.util.Map;

public interface ExcelReader {

    <T> ExcelSheetReader<T> type(Class<T> type);

    <T> AnnotationExcelSheetReader<T> annotation(Class<T> type);

    ExcelSheetReader<Map<String, Object>> map();

    <T> ExcelSheetReader<T> custom(Class<T> type, MetaAnalyzer<?> metaAnalyzer);

}
