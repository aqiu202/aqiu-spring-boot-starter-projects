package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.meta.MapPropertyMeta;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.excel.read.convert.NoneRowMappedCellValuesConverter;
import com.github.aqiu202.excel.read.convert.RowMappedCellValuesConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowExcelSheetReader extends SimpleExcelSheetReader<RowMappedCellValues> {

    public RowExcelSheetReader(ConverterFactory converterFactory, SheetReadConfiguration configuration, ExcelBeforeReadHandler beforeReadHandler) {
        super(RowMappedCellValues.class, new SimpleDataAnalyser(), converterFactory, configuration, beforeReadHandler);
    }

    @Override
    public RowMappedCellValuesConverter getRowMappedCellValuesConverter() {
        return NoneRowMappedCellValuesConverter.INSTANCE;
    }

    @Override
    public List<RowMappedCellValues> readSheetRows(Sheet sheet, int headRows) {
        SheetReadConfiguration configuration = this.getConfiguration();
        int minRowNum = sheet.getFirstRowNum();
        int maxRowNum = sheet.getLastRowNum();
        // 获取表内容第一行
        int contentFirstRowNum = minRowNum + headRows;
        // 获取表头末行(表内容前一行)
        Row headRow = sheet.getRow(Math.max(contentFirstRowNum - 1, 0));
        int columns = headRow.getPhysicalNumberOfCells();
        List<RowMappedCellValues> mappedCellValues = new ArrayList<>();
        for (int i = contentFirstRowNum; i <= maxRowNum; i++) {
            Row row = sheet.getRow(i);
            RowMappedCellValues rowCellValues = new SimpleRowMappedCellValues(i, columns);
            for (int colIndex = 0; colIndex < columns; colIndex++) {
                Cell headCell = headRow.getCell(colIndex);
                CellVal<?> headCellVal = this.dataAnalyser.readConvertedCellValue(headCell, configuration, null);
                HeadMeta headMeta = new HeadMeta(colIndex, headCellVal);
                Cell cell = row.getCell(colIndex);
                CellVal<?> cellVal = this.dataAnalyser.readConvertedCellValue(cell, configuration, null);
                // 空/未知数据跳过不处理
                if (cellVal instanceof NullCellVal || cellVal instanceof BlankCellVal
                        || cellVal instanceof UnknownCellVal) {
                    continue;
                }
                rowCellValues.setMappedCellValue(colIndex, new SimpleMappedCellValue(new MapPropertyMeta(headMeta.asText()), cellVal));
            }
            mappedCellValues.add(rowCellValues);
        }
        return mappedCellValues;
    }


}
