package com.github.aqiu202.excel.write;

import java.util.Objects;

public class CellIndex {

    private final int rowIndex;
    private final int colIndex;

    public CellIndex(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellIndex cellIndex = (CellIndex) o;
        return rowIndex == cellIndex.rowIndex && colIndex == cellIndex.colIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, colIndex);
    }
}
