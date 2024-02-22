package com.github.aqiu202.excel.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.ExcelStyleBuilder;
import org.apache.poi.ss.usermodel.Row;

public interface RowHandler {

    void handle(int index, Row row, Object value, ExcelStyleBuilder styleBuilder, SheetWriteConfiguration configuration);
}
