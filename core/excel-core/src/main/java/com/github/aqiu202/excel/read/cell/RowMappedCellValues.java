package com.github.aqiu202.excel.read.cell;

public interface RowMappedCellValues {

    int getRowNum();

    MappedCellValue[] getMappedCellValues();

    default MappedCellValue getMappedCellValue(int index) {
        return this.getMappedCellValues()[index];
    }

    default void setMappedCellValue(int index, MappedCellValue mappedCellValue) {
        this.getMappedCellValues()[index] = mappedCellValue;
    }

}
