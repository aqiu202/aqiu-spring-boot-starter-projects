package com.github.aqiu202.excel.model;

public class WorkbookSheetWriteConfiguration extends SheetWriteConfiguration {

    public static final int DEFAULT_ROW_ACCESS_WINDOW_SIZE = 2000;

    private WorkbookType workBookType = WorkbookType.SXSSF;

    private int rowAccessWindowSize = DEFAULT_ROW_ACCESS_WINDOW_SIZE;

    public WorkbookType getWorkBookType() {
        return workBookType;
    }

    public void setWorkBookType(WorkbookType workBookType) {
        this.workBookType = workBookType;
    }

    public int getRowAccessWindowSize() {
        return rowAccessWindowSize;
    }

    public void setRowAccessWindowSize(int rowAccessWindowSize) {
        this.rowAccessWindowSize = rowAccessWindowSize;
    }
}
