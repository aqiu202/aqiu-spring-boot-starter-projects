package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;

import javax.annotation.Nullable;

public interface FieldValueSetter {
    void set(Object instance, @Nullable MappedCellValue mappedCellValue);

    default Object extractCellValueWithType(CellVal<?> cellVal, Class<?> type) {
        // 如果是Map直接返回value
        if (type == null) {
            return cellVal.getValue();
        }
        Object targetValue;
        if (ClassUtils.isNumber(type) && cellVal instanceof NumberCellVal) {
            targetValue = ((NumberCellVal) cellVal).convertAs(type);
        } else if (ClassUtils.isDate(type) && cellVal instanceof DateCellVal) {
            targetValue = ((DateCellVal) cellVal).convertAs(type);
        } else if ((type.equals(Boolean.class) || type.equals(Boolean.TYPE)) && cellVal instanceof BooleanCellVal) {
            targetValue = ((BooleanCellVal) cellVal).getValue();
        } else if (type.equals(String.class)) {
            targetValue = cellVal.getValue();
        } else {
            targetValue = null;
        }
        return targetValue;
    }
}
