package com.github.aqiu202.excel.model;

import java.util.List;

public class SheetData<T> {

    private String sheetName;
    private Class<T> type;
    private List<T> data;
    private SheetWriteConfiguration configuration;


    public SheetData() {
    }

    public SheetData(String sheetName, Class<T> type, List<T> data, SheetWriteConfiguration configuration) {
        this.sheetName = sheetName;
        this.type = type;
        this.data = data;
        this.configuration = configuration;
    }
    public SheetData(String sheetName, Class<T> type, List<T> data) {
        this(sheetName, type, data, null);
    }

    public SheetWriteConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SheetWriteConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
