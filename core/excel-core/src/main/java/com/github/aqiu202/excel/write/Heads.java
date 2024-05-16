package com.github.aqiu202.excel.write;

import javax.annotation.Nullable;
import java.util.List;

public interface Heads {

    @Nullable
    HeadItem getHead(CellIndex index);

    @Nullable
    default HeadItem getHead(int rowIndex, int colIndex) {
        return this.getHead(new CellIndex(rowIndex, colIndex));
    }

    List<HeadItem> getAllHeadItems();

    int getHeadColumns();

    int getHeadRows();
}
