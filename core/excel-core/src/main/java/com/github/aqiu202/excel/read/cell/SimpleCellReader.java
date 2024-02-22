package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class SimpleCellReader implements CellReader {

    @Override
    public CellValue<?> readCell(Cell cell) {
        if (cell==null) return new StringCellValue(cell, null);
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return new StringCellValue(cell, cell.getStringCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new DateCellValue(cell, cell.getLocalDateTimeCellValue());
                } else {
                    return new NumberCellValue(cell, cell.getNumericCellValue());
                }
            case BOOLEAN:
                return new BooleanCellValue(cell, cell.getBooleanCellValue());
            case FORMULA:
                return new FormulaCellValue(cell, cell.getCellFormula());
            default:
                return new UnknownCellValue(cell);
        }
    }
}
