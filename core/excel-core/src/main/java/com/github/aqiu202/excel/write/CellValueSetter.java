package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import org.apache.poi.ss.usermodel.Cell;

public interface CellValueSetter {

    void setCellValue(Cell cell, ValueWrapper<?> result, StyleProcessor styleProcessor, SheetWriteConfiguration configuration);
}
