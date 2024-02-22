package com.github.aqiu202.excel.appender;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.TypeDataExtractor;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

public class SimpleExcelAppender implements ExcelAppender {

    private final WorkbookAppender workbookAppender;

    public SimpleExcelAppender() {
        this(new TypeDataExtractor());
    }

    public SimpleExcelAppender(DataExtractor<?> dataExtractor) {
        this.workbookAppender = new SimpleWorkbookAppender(dataExtractor);
    }

    public SimpleExcelAppender(DataExtractor<?> dataExtractor, ConverterFactory converterFactory) {
        this.workbookAppender = new SimpleWorkbookAppender(dataExtractor, converterFactory);
    }

    private Workbook workbook;

    public WorkbookAppender getWorkbookAppender() {
        return workbookAppender;
    }

    @Override
    public SheetWriteConfiguration getConfiguration() {
        return this.getWorkbookAppender().getConfiguration();
    }

    @Override
    public ExcelAppender configuration(SheetWriteConfiguration configuration) {
        this.getWorkbookAppender().configuration(configuration);
        return this;
    }

    @Override
    public ExcelAppender append(SheetData<?> sheetData) {
        this.getWorkbookAppender().append(this.getWorkbook(), sheetData);
        return this;
    }

    @Override
    public ExcelAppender append(SheetData<?> sheetData, int startRowIndex) {
        this.getWorkbookAppender().append(this.getWorkbook(), sheetData, startRowIndex);
        return this;
    }

    @Override
    public ExcelAppender append(Collection<SheetData<?>> sheetDataCollection) {
        for (SheetData<?> sheetData : sheetDataCollection) {
            this.append(sheetData);
        }
        return this;
    }

    @Override
    public <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, SheetData<?> sheetData) {
        this.workbookAppender.append(this.getWorkbook(), dataExtractor, sheetData);
        return this;
    }

    @Override
    public <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, SheetData<?> sheetData, int startRowIndex) {
        this.workbookAppender.append(this.getWorkbook(), dataExtractor, sheetData, startRowIndex);
        return this;
    }

    @Override
    public <T extends MappingMeta> ExcelAppender append(DataExtractor<T> dataExtractor, Collection<SheetData<?>> sheetDataCollection) {
        for (SheetData<?> sheetData : sheetDataCollection) {
            this.append(dataExtractor, sheetData);
        }
        return this;
    }

    @Override
    public Workbook getWorkbook() {
        if (this.workbook == null) {
            this.workbook = this.getWorkbookAppender().createWorkbook();
        }
        return this.workbook;
    }

    @Override
    public ExcelAppender dataExtractor(DataExtractor<?> dataExtractor) {
        this.getWorkbookAppender().setDataExtractor(dataExtractor);
        return this;
    }

    @Override
    public DataExtractor<?> getDataExtractor() {
        return this.getWorkbookAppender().getDataExtractor();
    }

    @Override
    public ExcelAppender converterFactory(ConverterFactory converterFactory) {
        this.getWorkbookAppender().setConverterFactory(converterFactory);
        return this;
    }

    @Override
    public ConverterFactory getConverterFactory() {
        return this.getWorkbookAppender().getConverterFactory();
    }
}
