package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class BlankCellVal extends SimpleCellVal<Void> {

    public BlankCellVal(Cell cell) {
        super(cell, null);
    }
}
