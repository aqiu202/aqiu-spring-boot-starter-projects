package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.Nullable;

public interface CellVal<T> {

    @Nullable
    Cell getCell();

    T getValue();

    default String getValueAsText() {
        return String.valueOf(this.getValue());
    }

}
