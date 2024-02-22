package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.read.cell.CellValue;
import com.github.aqiu202.util.StringUtils;

/**
 * 映射元数据（描述字段信息，包括与表头的对应关系）
 */
public interface MappingMeta {

    String getFieldName();

    String getFieldTitle();

    default boolean match(CellValue<?> metaCellValue) {
        Object value = metaCellValue.getValue();
        if (value == null) {
            return false;
        }
        String cellValue = String.valueOf(value);
        return StringUtils.equals(this.getFieldTitle(), cellValue)
                || StringUtils.equals(this.getFieldName(), cellValue);
    }
}
