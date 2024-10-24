package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.meta.DataMeta;

public interface MappedCellValue {

    DataMeta getTableMeta();

    CellVal<?> getCellValue();

    void setCellValue(CellVal<?> cellVal);
}
