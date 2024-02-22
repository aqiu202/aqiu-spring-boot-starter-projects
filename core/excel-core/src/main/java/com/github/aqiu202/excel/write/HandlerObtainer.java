package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.hand.CellHandler;
import com.github.aqiu202.excel.hand.RowHandler;
import com.github.aqiu202.excel.hand.SheetHandler;

public interface HandlerObtainer {

    SheetHandler obtainSheetHandler();

    RowHandler obtainRowHandler();

    CellHandler obtainCellHandler();

    void sheetHandler(SheetHandler sheetHandler);

    void rowHandler(RowHandler rowHandler);

    void cellHandler(CellHandler cellHandler);

}
