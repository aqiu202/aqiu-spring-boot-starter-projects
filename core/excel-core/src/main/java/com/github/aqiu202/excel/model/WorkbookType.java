package com.github.aqiu202.excel.model;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum WorkbookType {
    /**
     * Office 2003及之前的版本
     */
    HSSF(HSSFWorkbook.class),
    /**
     * Office 2007及以后版本
     */
    XSSF(XSSFWorkbook.class),
    /**
     * 为了防止导出文件过大导致OOM异常，基于XSSF扩展，
     * 超过rowAccessWindowSize将自动将数据从内存落到磁盘，
     * 因此可能会导致Row数据无法再读取的情况
     * （推荐）
     */
    SXSSF(SXSSFWorkbook.class);

    private final Class<? extends Workbook> type;

    WorkbookType(Class<? extends Workbook> type) {
        this.type = type;
    }

    public Class<? extends Workbook> getType() {
        return type;
    }
}
