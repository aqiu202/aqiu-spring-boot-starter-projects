package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.meta.DataMeta;

public class SimpleMappedCellValue implements MappedCellValue {
    private final DataMeta dataMeta;

    private CellVal<?> cellVal;

    public SimpleMappedCellValue(DataMeta dataMeta, CellVal<?> cellVal) {
        this.dataMeta = dataMeta;
        this.cellVal = cellVal;
    }

    @Override
    public DataMeta getTableMeta() {
        return dataMeta;
    }

    @Override
    public CellVal<?> getCellValue() {
        return cellVal;
    }

    @Override
    public void setCellValue(CellVal<?> cellVal) {
        this.cellVal = cellVal;
    }
}
