package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.meta.TableMeta;

public class SimpleMappedCellValue implements MappedCellValue {
    private final TableMeta tableMeta;

    private CellVal<?> cellVal;

    public SimpleMappedCellValue(TableMeta tableMeta, CellVal<?> cellVal) {
        this.tableMeta = tableMeta;
        this.cellVal = cellVal;
    }

    @Override
    public TableMeta getTableMeta() {
        return tableMeta;
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
