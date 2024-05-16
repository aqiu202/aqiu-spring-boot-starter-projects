package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import org.apache.poi.ss.usermodel.Row;

public class RowHandlers extends SimpleHandlers<RowHandler> implements RowHandler, Handlers<RowHandler> {

    @Override
    public void handle(int index, Row row, Object value, StyleProcessor styleProcessor, SheetWriteConfiguration configuration) {
        for (RowHandler handler : this.getHandlers()) {
            handler.handle(index, row, value, styleProcessor, configuration);
        }
    }
}
