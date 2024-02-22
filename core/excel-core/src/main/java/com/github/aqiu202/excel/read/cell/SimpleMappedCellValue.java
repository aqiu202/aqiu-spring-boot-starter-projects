package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.prop.MappedProperty;

public class SimpleMappedCellValue implements MappedCellValue{
    private final MappedProperty mappedProperty;

    private CellValue<?> cellValue;

    public SimpleMappedCellValue(MappedProperty mappedProperty, CellValue<?> cellValue) {
        this.mappedProperty = mappedProperty;
        this.cellValue = cellValue;
    }

    @Override
    public MappedProperty getMappedProperty() {
        return mappedProperty;
    }

    @Override
    public CellValue<?> getCellValue() {
        return cellValue;
    }

    @Override
    public void setCellValue(CellValue<?> cellValue) {
        this.cellValue = cellValue;
    }
}
