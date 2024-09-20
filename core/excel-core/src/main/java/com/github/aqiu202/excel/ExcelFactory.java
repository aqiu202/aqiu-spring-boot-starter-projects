package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactoryWrapper;
import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;

public interface ExcelFactory extends ConverterFactoryWrapper<ExcelFactory> {

    ExcelReaderBuilder buildReader();

    ExcelWriterBuilder buildWriter();
}
