package com.github.aqiu202.excel.hand;

import com.github.aqiu202.excel.format.wrap.ResultWrapper;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.ExcelStyleBuilder;
import org.apache.poi.ss.usermodel.Cell;

public interface CellHandler {

    void handle(int row, int col, Cell cell, ResultWrapper<?> value, ExcelStyleBuilder styleBuilder,
                SheetWriteConfiguration configuration);

}
