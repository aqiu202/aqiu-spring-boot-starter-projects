package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.read.cell.MappedCellValue;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;
import com.github.aqiu202.util.ClassUtils;

public class SimpleTypeTranslator implements TypeTranslator {

    private final FieldValueSetter fieldValueSetter;

    public SimpleTypeTranslator() {
        this(new ComplexFieldValueSetter());
    }

    public SimpleTypeTranslator(FieldValueSetter fieldValueSetter) {
        this.fieldValueSetter = fieldValueSetter;
    }

    @Override
    public <T> T translate(RowMappedCellValues rowMappedCellValues, Class<T> type) {
        // 读取的类必须有默认构造器
        T result = ClassUtils.newInstance(type);
        MappedCellValue[] mappedCellValues = rowMappedCellValues.getMappedCellValues();
        for (MappedCellValue mappedCellValue : mappedCellValues) {
            this.fieldValueSetter.set(result, mappedCellValue);
        }
        return result;
    }
}
