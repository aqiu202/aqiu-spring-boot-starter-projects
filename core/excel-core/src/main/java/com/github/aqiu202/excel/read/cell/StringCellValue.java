package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class StringCellValue extends SimpleCellValue<String> {
    public StringCellValue(Cell cell, String value) {
        super(cell, value);
    }
}
