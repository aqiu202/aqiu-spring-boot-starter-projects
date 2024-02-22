package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactoryWrapper;
import com.github.aqiu202.excel.model.SheetDataConfiguration;
import com.github.aqiu202.excel.read.ExcelReaderBuilder;
import com.github.aqiu202.excel.write.ExcelWriterBuilder;

public interface ExcelFactory extends ConverterFactoryWrapper<ExcelFactory> {

    SheetDataConfiguration getConfiguration();

    ExcelReaderBuilder forReader();

    ExcelWriterBuilder forWriter();
}
