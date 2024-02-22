package com.github.aqiu202.excel.appender;

import com.github.aqiu202.excel.convert.ConverterFactoryWrapper;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.DataExtractorWrapper;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.write.ConfigurableExcelWriter;

import java.util.Collection;

public interface ExcelAppender extends ConfigurableExcelWriter<ExcelAppender>, WorkbookWrapper,
        DataExtractorWrapper<ExcelAppender>, ConverterFactoryWrapper<ExcelAppender> {

    ExcelAppender append(SheetData<?> sheetData);
    ExcelAppender append(SheetData<?> sheetData, int startRowIndex);

    ExcelAppender append(Collection<SheetData<?>> sheetDataCollection);

    <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, SheetData<?> sheetData);

    <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, SheetData<?> sheetData, int startRowIndex);

    <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, Collection<SheetData<?>> sheetDataCollection);

}
