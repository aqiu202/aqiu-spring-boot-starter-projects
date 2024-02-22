package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class BooleanCellValue extends SimpleCellValue<Boolean> {
    public BooleanCellValue(Cell cell, Boolean value) {
        super(cell, value);
    }
}
