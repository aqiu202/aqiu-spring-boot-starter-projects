package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import org.apache.poi.ss.usermodel.Cell;

public interface CellHandler {

    void handleHead(int row, int col, Cell cell, ValueWrapper<?> value, StyleProcessor styleProcessor,
                    SheetWriteConfiguration configuration);

    void handleContent(int row, int col, Cell cell, ValueWrapper<?> value, StyleProcessor styleProcessor,
                       SheetWriteConfiguration configuration);

}
