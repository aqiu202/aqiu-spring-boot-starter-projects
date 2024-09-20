package com.github.aqiu202.excel.write;

import java.util.Collection;

public interface ExcelSheetWriter<T> extends ExportableWorkbookWrapper {

    String SHEET_NAME_PREFIX = "Sheet";

    ExcelSheetConfigurer<T> getConfigurer();

    ExcelSheetWriter<T> write(Collection<T> data);

    ChainedExcelWriter next();

    <S> ExcelSheetConfigurer<S> nextSheet(Class<S> type);

}
