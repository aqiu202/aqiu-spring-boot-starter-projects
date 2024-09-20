package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

public class SheetHandlers extends SimpleHandlers<SheetHandler> implements SheetHandler, Handlers<SheetHandler> {

    @Override
    public void handle(Sheet sheet, int columnCount, Collection<?> data, SheetWriteConfiguration configuration) {
        for (SheetHandler handler : this.getHandlers()) {
            handler.handle(sheet, columnCount, data, configuration);
        }
    }
}
