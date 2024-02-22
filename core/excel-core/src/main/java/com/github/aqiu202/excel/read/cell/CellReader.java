package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Excel单元格读取器
 */
public interface CellReader {

    CellValue<?> readCell(Cell cell);
}
