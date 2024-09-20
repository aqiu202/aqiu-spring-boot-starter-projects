package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class BooleanCellVal extends SimpleCellVal<Boolean> {
    public BooleanCellVal(Cell cell, Boolean value) {
        super(cell, value);
    }
}
