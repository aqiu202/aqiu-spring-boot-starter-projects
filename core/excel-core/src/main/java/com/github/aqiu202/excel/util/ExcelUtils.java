package com.github.aqiu202.excel.util;

import com.github.aqiu202.excel.ExcelFactory;
import com.github.aqiu202.excel.read.ExcelReader;
import com.github.aqiu202.excel.write.ExcelWriter;

public class ExcelUtils {

    private static final ExcelFactory GLOBAL_EXCEL_FACTORY = ExcelFactory.builder().build();

    private static final ExcelReader GLOBAL_EXCEL_READER = GLOBAL_EXCEL_FACTORY.buildReader().build();

    private static final ExcelWriter GLOBAL_EXCEL_WRITER = GLOBAL_EXCEL_FACTORY.buildWriter().build();

    public static ExcelReader getDefaultExcelReader() {
        return GLOBAL_EXCEL_READER;
    }

    public static ExcelWriter getDefaultExcelWriter() {
        return GLOBAL_EXCEL_WRITER;
    }
}
