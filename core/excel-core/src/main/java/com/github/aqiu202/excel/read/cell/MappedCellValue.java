package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.prop.MappedProperty;

public interface MappedCellValue {

    MappedProperty getMappedProperty();

    CellValue<?> getCellValue();

    void setCellValue(CellValue<?> cellValue);
}
