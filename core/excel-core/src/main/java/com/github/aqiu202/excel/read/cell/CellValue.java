package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValue<T> {

    Cell getCell();

    T getValue();

}
