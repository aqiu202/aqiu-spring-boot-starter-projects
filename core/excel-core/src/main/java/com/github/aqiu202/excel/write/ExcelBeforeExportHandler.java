package com.github.aqiu202.excel.write;

import java.io.OutputStream;

@FunctionalInterface
public interface ExcelBeforeExportHandler {

    OutputStream handle(OutputStream os) throws Exception;
}
