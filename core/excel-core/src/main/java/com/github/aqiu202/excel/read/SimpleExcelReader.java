package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.mapping.AnnotationValueMapping;
import com.github.aqiu202.excel.mapping.MapValueMapping;
import com.github.aqiu202.excel.mapping.ValueMapping;

import java.util.HashMap;
import java.util.Map;

public class SimpleExcelReader implements ExcelReader {
    @Override
    public <T> CommonExcelReader<T> type(Class<T> type) {
        return new SimpleCommonExcelReader<>(type);
    }

    @Override
    public <T> CommonExcelReader<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new SimpleCommonExcelReader<>(type, new AnnotationValueMapping());
    }

    @Override
    public <T> CommonExcelReader<T> custom(Class<T> type, ValueMapping<?> valueMapping) {
        return new SimpleCommonExcelReader<>(type, valueMapping);
    }

    @Override
    public CommonExcelReader<HashMap> map() {
        return this.map(HashMap.class);
    }

    @Override
    public <T extends Map> CommonExcelReader<T> map(Class<T> type) {
        return new SimpleCommonExcelReader<>(type, new MapValueMapping());
    }
}
