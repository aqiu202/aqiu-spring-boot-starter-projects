package com.github.aqiu202.excel.write;

import java.util.Collection;

public interface ItemExcelWriter<T> {

    ItemExcelWriter<T> sheetName(String sheetName);

    TypedExcelWriter writeData(Collection<T> data);

    default TypedExcelWriter write(String sheetName, Collection<T> data) {
        this.sheetName(sheetName);
        return this.writeData(data);
    }

}
