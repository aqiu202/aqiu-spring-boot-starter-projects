package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.hand.*;

public class SimpleHandlerObtainer implements HandlerObtainer {

    private SheetHandler sheetHandler = new SimpleSheetHandler();
    private RowHandler rowHandler = new SimpleRowHandler();
    private CellHandler cellHandler = new SimpleCellHandler();

    @Override
    public SheetHandler obtainSheetHandler() {
        return this.sheetHandler;
    }

    @Override
    public RowHandler obtainRowHandler() {
        return this.rowHandler;
    }

    @Override
    public CellHandler obtainCellHandler() {
        return this.cellHandler;
    }

    @Override
    public void sheetHandler(SheetHandler sheetHandler) {
        this.sheetHandler = sheetHandler;
    }

    @Override
    public void rowHandler(RowHandler rowHandler) {
        this.rowHandler = rowHandler;
    }

    @Override
    public void cellHandler(CellHandler cellHandler) {
        this.cellHandler = cellHandler;
    }
}
