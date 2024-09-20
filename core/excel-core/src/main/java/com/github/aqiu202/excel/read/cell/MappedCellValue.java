package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.meta.TableMeta;

public interface MappedCellValue {

    TableMeta getTableMeta();

    CellVal<?> getCellValue();

    void setCellValue(CellVal<?> cellVal);
}
