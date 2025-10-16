package com.github.aqiu202.excel.read;

import java.io.InputStream;

@FunctionalInterface
public interface ExcelBeforeReadHandler {

    InputStream handle(InputStream is) throws Exception;
}
