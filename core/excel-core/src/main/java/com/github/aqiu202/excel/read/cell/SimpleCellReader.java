package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.model.ReadConfiguration;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.*;

public class SimpleCellReader implements CellReader {

    @Override
    public CellVal<?> readCell(Cell cell, ReadConfiguration configuration) {
        if (cell == null) {
            return new NullCellVal();
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                String stringCellValue = cell.getStringCellValue();
                if (!configuration.isReadEmptyText() && StringUtils.isEmpty(stringCellValue)) {
                    return new BlankCellVal(cell);
                }
                return new StringCellVal(cell, stringCellValue);
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateCellVal.of(cell, cell.getDateCellValue());
                } else {
                    return new NumberCellVal(cell, cell.getNumericCellValue());
                }
            case BOOLEAN:
                return new BooleanCellVal(cell, cell.getBooleanCellValue());
            case FORMULA:
                return configuration.isReadFormula() ? new FormulaCellVal(cell, this.readFormulaValue(cell)) : new BlankCellVal(cell);
            case BLANK:
                return new BlankCellVal(cell);
            default:
                return new UnknownCellVal(cell);
        }
    }

    public CellVal<?> readFormulaValue(Cell cell) {
        Workbook workbook = cell.getRow().getSheet().getWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        FormulaEvaluator formulaEvaluator = creationHelper.createFormulaEvaluator();
        CellValue cellValue = formulaEvaluator.evaluate(cell);
        CellType cellType = cellValue.getCellType();
        switch (cellType) {
            case STRING:
                return new StringCellVal(cell, cellValue.getStringValue());
            case NUMERIC:
                return new NumberCellVal(cell, cellValue.getNumberValue());
            case BOOLEAN:
                return new BooleanCellVal(cell, cellValue.getBooleanValue());
            default:
                return new UnknownCellVal(cell);
        }
    }

    @Override
    public HeadMeta readHead(Sheet sheet, int colIndex, int headRows) {
        CellVal<?>[] cvs = new CellVal[headRows];
        for (int i = 0; i < headRows; i++) {
            Row row = sheet.getRow(i);
            Cell cell;
            if (row == null) {
                cell = null;
            } else {
                cell = row.getCell(colIndex);
            }
            cvs[i] = this.readCell(cell);
        }
        return new HeadMeta(colIndex, cvs);
    }
}
