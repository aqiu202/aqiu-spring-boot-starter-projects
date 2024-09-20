package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.Nullable;

public class SimpleCellVal<T> implements CellVal<T> {

    private final Cell cell;
    private final T value;

    public SimpleCellVal(Cell cell, T value) {
        this.cell = cell;
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Nullable
    @Override
    public Cell getCell() {
        return this.cell;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
