package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.SimpleSwitchableDataExtractor;
import com.github.aqiu202.excel.extract.SwitchableDataExtractor;
import com.github.aqiu202.excel.model.DataExtractMode;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Collection;

public class SimpleSheetExcelWriter extends SimpleCombineExcelWriter<SheetExcelWriter> implements SheetExcelWriter {

    private final String sheetName;

    public SimpleSheetExcelWriter(String sheetName) {
        super(new SimpleSwitchableDataExtractor<>());
        this.sheetName = sheetName;
    }

    public SimpleSheetExcelWriter(String sheetName, SheetWriteConfiguration configuration) {
        super(new SimpleSwitchableDataExtractor<>());
        super.configuration(configuration);
        this.sheetName = sheetName;
    }

    public SimpleSheetExcelWriter(String sheetName, SheetWriteConfiguration configuration, ConverterFactory converterFactory) {
        super(new SimpleSwitchableDataExtractor<>(), converterFactory);
        super.configuration(configuration);
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    @Override
    public <T> Workbook writeWorkbook(Class<T> type, Collection<T> data) {
        SheetData<T> sheetData = this.buildSheetData(type, data);
        return this.getExcelAppender()
                .append(sheetData).getWorkbook();
    }

    @Override
    public <T> Workbook writeWorkbook(Class<T> type, Collection<T> data, int startRowIndex) {
        SheetData<T> sheetData = this.buildSheetData(type, data);
        return this.getExcelAppender()
                .append(sheetData, startRowIndex).getWorkbook();
    }

    protected <T> SheetData<T> buildSheetData(Class<T> type, Collection<T> data) {
        return new SheetData<>(this.sheetName, type, new ArrayList<>(data), this.getConfiguration());
    }

    @Override
    public SheetExcelWriter extractMode(DataExtractMode extractMode) {
        DataExtractor<?> dataExtractor = this.getDataExtractor();
        if (dataExtractor instanceof SwitchableDataExtractor) {
            ((SwitchableDataExtractor<?>) dataExtractor).extractMode(extractMode);
        }
        return this;
    }
}
