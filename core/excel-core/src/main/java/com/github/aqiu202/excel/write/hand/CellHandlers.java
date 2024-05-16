package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import org.apache.poi.ss.usermodel.Cell;

public class CellHandlers extends SimpleHandlers<CellHandler> implements CellHandler, Handlers<CellHandler> {

    @Override
    public void handleHead(int row, int col, Cell cell, ValueWrapper<?> value, StyleProcessor styleProcessor, SheetWriteConfiguration configuration) {
        for (CellHandler handler : this.getHandlers()) {
            handler.handleHead(row, col, cell, value, styleProcessor, configuration);
        }
    }

    @Override
    public void handleContent(int row, int col, Cell cell, ValueWrapper<?> value, StyleProcessor styleProcessor, SheetWriteConfiguration configuration) {
        for (CellHandler handler : this.getHandlers()) {
            handler.handleContent(row, col, cell, value, styleProcessor, configuration);
        }
    }
}
