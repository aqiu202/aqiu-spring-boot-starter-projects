package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.Converter;
import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.model.ReadConfiguration;
import com.github.aqiu202.excel.read.cell.CellVal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface DataAnalyser {

    CellVal<?> readConvertedCellValue(Cell cell, ReadConfiguration configuration, Converter converter);

    List<IndexedTableMeta> analyse(Sheet sheet, Class<?> type, int startColIndex, int columns, int headRows);

}
