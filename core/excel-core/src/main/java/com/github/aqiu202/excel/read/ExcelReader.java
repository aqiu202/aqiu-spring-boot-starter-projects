package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.FieldMetaAnalyzer;
import com.github.aqiu202.excel.analyse.MetaAnalyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ExcelReader {

    default <T> ExcelSheetReader<T> type(Class<T> type) {
        return this.custom(type, FieldMetaAnalyzer.INSTANCE);
    }

    <T> AnnotationExcelSheetReader<T> annotation(Class<T> type);

    default <T> ExcelSheetReader<T> index(List<String> fieldProperties, Class<T> type) {
        return this.custom(type, new IndexedDataAnalyser(fieldProperties));
    }

    default ExcelSheetReader<Map<String, Object>> map() {
        return ((ExcelSheetReader) this.type(LinkedHashMap.class));
    }

    RowExcelSheetReader rows();

    default <T> ExcelSheetReader<T> custom(Class<T> type, MetaAnalyzer<?> metaAnalyzer) {
        return this.custom(type, new SimpleDataAnalyser(metaAnalyzer));
    }

    <T> ExcelSheetReader<T> custom(Class<T> type, DataAnalyser dataAnalyser);

}
