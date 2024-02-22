package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.mapping.FieldValueMapping;
import com.github.aqiu202.excel.mapping.IndexValueMapping;
import com.github.aqiu202.excel.mapping.ValueMapping;

import java.util.HashMap;
import java.util.Map;

public interface ExcelReader {
    <T> CommonExcelReader<T> type(Class<T> type);

    <T> CommonExcelReader<T> annotation(Class<T> type);

    <T> CommonExcelReader<T> custom(Class<T> type, ValueMapping<?> valueMapping);

    default <T> CommonExcelReader<T> index(Class<T> type, IndexValueMapping valueMapping) {
        return this.custom(type, valueMapping);
    }

    default <T> CommonExcelReader<T> property(Class<T> type, FieldValueMapping valueMapping) {
        return this.custom(type, valueMapping);
    }

    <T extends Map> CommonExcelReader<T> map(Class<T> type);

    CommonExcelReader<HashMap> map();

}
