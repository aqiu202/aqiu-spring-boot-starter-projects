package com.github.aqiu202.excel.appender;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.TypeDataExtractor;
import com.github.aqiu202.excel.format.wrap.ResultWrapper;
import com.github.aqiu202.excel.format.wrap.SimpleStringResultWrapper;
import com.github.aqiu202.excel.hand.CellHandler;
import com.github.aqiu202.excel.hand.RowHandler;
import com.github.aqiu202.excel.hand.SheetHandler;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.style.ExcelStyleBuilder;
import com.github.aqiu202.excel.style.SimpleExcelStyleBuilder;
import com.github.aqiu202.excel.write.ExcelWriter;
import com.github.aqiu202.excel.write.SimpleConfigurableHandlerExcelWriter;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public class SimpleWorkbookAppender extends SimpleConfigurableHandlerExcelWriter<WorkbookAppender> implements WorkbookAppender {

    private DataExtractor<?> dataExtractor;
    private ConverterFactory converterFactory;

    public SimpleWorkbookAppender() {
        this(new TypeDataExtractor(), SimpleConverterFactory.INSTANCE);
    }

    public SimpleWorkbookAppender(DataExtractor<?> dataReader) {
        this(dataReader, SimpleConverterFactory.INSTANCE);
    }

    public SimpleWorkbookAppender(DataExtractor<?> dataReader, ConverterFactory converterFactory) {
        this.dataExtractor = dataReader;
        this.converterFactory = converterFactory;
    }

    @Override
    public SimpleWorkbookAppender configuration(SheetWriteConfiguration configuration) {
        super.configuration(configuration);
        return this;
    }

    @Override
    public Workbook createWorkbook() {
        return this.createWorkbook(this.getConfiguration().getWorkBookType());
    }

    @Override
    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    @Override
    public void setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public Workbook createWorkbook(WorkbookType type) {
        if (WorkbookType.SXSSF.equals(type)) {
            return ClassUtils.newInstance(type.getType(), new Class[]{int.class},
                    new Integer[]{this.getRowAccessWindowSize()});
        }
        return ClassUtils.newInstance(type.getType());
    }

    @Override
    public void append(Workbook workbook, SheetData<?> data) {
        this.append(workbook, this.getDataExtractor(), data);
    }

    @Override
    public void append(Workbook workbook, SheetData<?> data, int startRowIndex) {
        this.append(workbook, this.getDataExtractor(), data, startRowIndex);
    }

    @Override
    public <T extends MappingMeta> void append(Workbook workbook, DataExtractor<T> dataExtractor, SheetData<?> data) {
        this.append(workbook, dataExtractor, data, 0);
    }

    @Override
    public <T extends MappingMeta> void append(Workbook workbook, DataExtractor<T> dataExtractor, SheetData<?> data, int startRowIndex) {
        ExcelStyleBuilder excelStyleBuilder = new SimpleExcelStyleBuilder(workbook);
        SheetWriteConfiguration customConfiguration = data.getConfiguration();
        SheetWriteConfiguration configuration = this.getConfiguration();
        if (customConfiguration != null) {
            configuration = customConfiguration;
        }
        int sheetIndex = workbook.getNumberOfSheets();
        String sheetName = data.getSheetName();
        Sheet sheet;
        if (ExcelWriter.SHEET_NAME_PREFIX.equals(sheetName)) {
            int newSheetIndex = sheetIndex + 1;
            sheet = workbook.createSheet(sheetName + newSheetIndex);
        } else {
            sheet = workbook.createSheet(sheetName);
        }
        List<T> metaList = dataExtractor.analysisMetas(data.getType());
        Row headRow = sheet.createRow(startRowIndex);
        RowHandler rowHandler = this.obtainRowHandler();
        if (rowHandler != null) {
            rowHandler.handle(startRowIndex, headRow, metaList, excelStyleBuilder, configuration);
        }
        startRowIndex++;
        CellHandler cellHandler = this.obtainCellHandler();
        for (int i = 0; i < metaList.size(); i++) {
            T meta = metaList.get(i);
            String title = meta.getFieldTitle();
            Cell cell = headRow.createCell(i);
            if (cellHandler != null) {
                cellHandler.handle(0, i, cell, new SimpleStringResultWrapper(title), excelStyleBuilder, configuration);
            }
        }
        List<?> rows = data.getData();
        for (Object item : rows) {
            Row row = sheet.createRow(startRowIndex);
            if (rowHandler != null) {
                rowHandler.handle(startRowIndex, headRow, item, excelStyleBuilder, configuration);
            }
            for (int j = 0; j < metaList.size(); j++) {
                Cell cell = row.createCell(j);
                T meta = metaList.get(j);
                ResultWrapper<?> value = dataExtractor.extractFormattedValue(meta, item, this.getConfiguration(),
                        this.getConverterFactory());
                if (cellHandler != null) {
                    cellHandler.handle(startRowIndex, j, cell, value, excelStyleBuilder, configuration);
                }
            }
            startRowIndex++;
        }
        SheetHandler sheetHandler = this.obtainSheetHandler();
        if (sheetHandler != null) {
            sheetHandler.handle(sheetIndex, sheet, metaList.size(), data.getData(), configuration);
        }
    }

    @Override
    public int getRowAccessWindowSize() {
        if (this.configuration != null) {
            return this.configuration.getRowAccessWindowSize();
        }
        return SheetWriteConfiguration.DEFAULT_ROW_ACCESS_WINDOW_SIZE;
    }

    @Override
    public void setDataExtractor(DataExtractor<?> dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    @Override
    public DataExtractor<?> getDataExtractor() {
        return this.dataExtractor;
    }
}
