package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class StringCellVal extends SimpleCellVal<String> {
    public StringCellVal(Cell cell, String value) {
        super(cell, value);
    }
}
