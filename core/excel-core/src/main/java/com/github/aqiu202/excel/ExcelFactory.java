package com.github.aqiu202.excel;

import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;

public interface ExcelFactory {

    ExcelReaderBuilder buildReader();

    ExcelWriterBuilder buildWriter();

    static ExcelFactoryBuilder builder() {
        return new ExcelFactoryBuilder();
    }
}
