package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;

public interface FieldValueSetter {
    void set(Object instance, MappedCellValue mappedCellValue);

    default Object extractCellValueWithType(CellValue<?> cellValue, Class<?> type) {
        // 如果是Map直接返回value
        if (type == null) {
            return cellValue.getValue();
        }
        Object targetValue;
        if (ClassUtils.isNumber(type) && cellValue instanceof NumberCellValue) {
            targetValue = ((NumberCellValue) cellValue).convertAs(type);
        } else if (ClassUtils.isDate(type) && cellValue instanceof DateCellValue) {
            targetValue = ((DateCellValue) cellValue).convertAs(type);
        } else if ((type.equals(Boolean.class) || type.equals(Boolean.TYPE)) && cellValue instanceof BooleanCellValue) {
            targetValue = ((BooleanCellValue) cellValue).getValue();
        } else if (type.equals(String.class)) {
            targetValue = cellValue.getValue();
        } else {
            targetValue = null;
        }
        return targetValue;
    }
}
