package com.github.aqiu202.excel.read.cell;

public class SimpleRowMappedCellValues implements RowMappedCellValues {

    private final MappedCellValue[] mappedCellValues;
    private final int rowNum;

    public SimpleRowMappedCellValues(int rowNum, int length) {
        this.rowNum = rowNum;
        this.mappedCellValues = new MappedCellValue[length];
    }

    @Override
    public int getRowNum() {
        return this.rowNum;
    }

    @Override
    public MappedCellValue[] getMappedCellValues() {
        return this.mappedCellValues;
    }
}
