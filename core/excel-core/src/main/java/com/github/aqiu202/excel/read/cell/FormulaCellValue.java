package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public class FormulaCellValue extends SimpleCellValue<String> {
    public FormulaCellValue(Cell cell, String value) {
        super(cell, value);
    }
}
