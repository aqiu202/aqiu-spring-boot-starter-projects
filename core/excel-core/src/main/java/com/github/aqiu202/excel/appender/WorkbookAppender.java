package com.github.aqiu202.excel.appender;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.write.ConfigurableExcelWriter;
import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookAppender extends ConfigurableExcelWriter<WorkbookAppender> {

    default Workbook createWorkbook() {
        return this.createWorkbook(WorkbookType.SXSSF);
    }

    int getRowAccessWindowSize();

    void setDataExtractor(DataExtractor<?> dataExtractor);

    DataExtractor<?> getDataExtractor();

    void setConverterFactory(ConverterFactory converterFactory);

    ConverterFactory getConverterFactory();

    Workbook createWorkbook(WorkbookType type);

    void append(Workbook workbook, SheetData<?> data);

    void append(Workbook workbook, SheetData<?> data, int startRowIndex);

    <T extends MappingMeta> void append(Workbook workbook, DataExtractor<T> dataExtractor, SheetData<?> data);

    <T extends MappingMeta> void append(Workbook workbook, DataExtractor<T> dataExtractor, SheetData<?> data, int startRowIndex);


}
