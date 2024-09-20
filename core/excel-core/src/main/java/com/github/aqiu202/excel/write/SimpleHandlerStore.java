package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.write.hand.*;

import javax.annotation.Nonnull;

public class SimpleHandlerStore implements HandlerStore {

    private final SheetHandlers sheetHandlers = new SheetHandlers();
    private final RowHandlers rowHandlers = new RowHandlers();
    private final CellHandlers cellHandlers = new CellHandlers();

    @Nonnull
    @Override
    public SheetHandler getResolvedSheetHandler() {
        return this.sheetHandlers;
    }

    @Nonnull
    @Override
    public RowHandler getResolvedRowHandler() {
        return this.rowHandlers;
    }

    @Nonnull
    @Override
    public CellHandler getResolvedCellHandler() {
        return this.cellHandlers;
    }

    @Override
    public void addSheetHandler(SheetHandler sheetHandler) {
        this.sheetHandlers.addHandler(sheetHandler);
    }

    @Override
    public void addRowHandler(RowHandler rowHandler) {
        this.rowHandlers.addHandler(rowHandler);
    }

    @Override
    public void addCellHandler(CellHandler cellHandler) {
        this.cellHandlers.addHandler(cellHandler);
    }
}
