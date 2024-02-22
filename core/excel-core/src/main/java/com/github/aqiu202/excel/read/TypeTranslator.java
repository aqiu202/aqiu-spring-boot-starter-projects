package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

public interface TypeTranslator {

    <T> T translate(RowMappedCellValues rowMappedCellValues, Class<T> type);

}
