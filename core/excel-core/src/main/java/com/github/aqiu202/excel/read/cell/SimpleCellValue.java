package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class SimpleCellValue<T> implements CellValue<T> {

    private final Cell cell;
    private final T value;

    public SimpleCellValue(Cell cell, T value) {
        this.cell = cell;
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Cell getCell() {
        return this.cell;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
