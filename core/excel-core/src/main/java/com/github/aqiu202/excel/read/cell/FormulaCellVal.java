package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class FormulaCellVal extends SimpleCellVal<CellVal<?>> {

    public FormulaCellVal(Cell cell, CellVal<?> value) {
        super(cell, value);
    }

}
