package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface RowHandler {

    void handle(int index, Row row, Object value, StyleProcessor styleProcessor, SheetWriteConfiguration configuration);
}
