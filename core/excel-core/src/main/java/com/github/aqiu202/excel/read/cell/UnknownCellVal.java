package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class UnknownCellVal extends SimpleCellVal<Void> {

    public UnknownCellVal(Cell cell) {
        super(cell, null);
    }
}
