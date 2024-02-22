package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class UnknownCellValue extends SimpleCellValue<Void> {
    public UnknownCellValue(Cell cell) {
        super(cell, null);
    }
}
