package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetData;

import java.util.ArrayList;
import java.util.Collection;

public class SimpleItemExcelWriter<T> implements ItemExcelWriter<T> {

    private String sheetName = ExcelWriter.SHEET_NAME_PREFIX;
    private final TypedExcelWriter writer;
    private final Class<T> dataType;

    public SimpleItemExcelWriter(TypedExcelWriter writer, Class<T> dataType) {
        this.writer = writer;
        this.dataType = dataType;
    }

    @Override
    public ItemExcelWriter<T> sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    @Override
    public TypedExcelWriter writeData(Collection<T> data) {
        return this.writer.writeItem(this.bulidSheetData(data));
    }

    protected SheetData<T> bulidSheetData(Collection<T> data) {
        return new SheetData<>(sheetName, dataType, new ArrayList<>(data));
    }
}
