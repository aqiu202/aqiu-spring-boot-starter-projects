package com.github.aqiu202.excel.write;

public interface ExcelWriter extends ConfigurableExcelWriter<ExcelWriter> {

    String SHEET_NAME_PREFIX = "sheet";

    <T> ItemExcelWriter<T> type(Class<T> type);

    <T> ItemExcelWriter<T> annotation(Class<T> type);

    SheetExcelWriter sheetName(String sheetName);

    BatchExcelWriter batch();

}
