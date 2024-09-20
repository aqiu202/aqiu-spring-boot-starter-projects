package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.write.hand.CellHandler;
import com.github.aqiu202.excel.write.hand.RowHandler;
import com.github.aqiu202.excel.write.hand.SheetHandler;

import javax.annotation.Nonnull;

public interface HandlerStore {

    @Nonnull
    SheetHandler getResolvedSheetHandler();

    @Nonnull
    RowHandler getResolvedRowHandler();

    @Nonnull
    CellHandler getResolvedCellHandler();

    void addSheetHandler(SheetHandler sheetHandler);

    void addRowHandler(RowHandler rowHandler);

    void addCellHandler(CellHandler cellHandler);

}
