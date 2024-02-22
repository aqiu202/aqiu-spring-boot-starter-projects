package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.extract.SimpleSwitchableDataExtractor;
import com.github.aqiu202.excel.extract.SwitchableDataExtractor;
import com.github.aqiu202.excel.model.DataExtractMode;
import com.github.aqiu202.excel.model.SheetData;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

public class SimpleBatchExcelWriter extends SimpleCombineExcelWriter<BatchExcelWriter> implements BatchExcelWriter {

    public SimpleBatchExcelWriter() {
    }

    public SimpleBatchExcelWriter(SheetWriteConfiguration configuration) {
        super(new SimpleSwitchableDataExtractor<>());
        super.configuration(configuration);
    }

    public SimpleBatchExcelWriter(SheetWriteConfiguration configuration, ConverterFactory converterFactory) {
        super(new SimpleSwitchableDataExtractor<>(), converterFactory);
        super.configuration(configuration);
    }

    @Override
    public Workbook write(Collection<SheetData<?>> dataCollection) {
        for (SheetData<?> sheetData : dataCollection) {
            this.writeItem(sheetData);
        }
        return this.getWorkbook();
    }

    @Override
    public BatchExcelWriter writeItem(DataExtractor<?> dataExtractor, SheetData<?> data, int startRowIndex) {
        this.getExcelAppender().append(dataExtractor, data, startRowIndex);
        return this;
    }

    @Override
    public BatchExcelWriter writeItem(SheetData<?> data) {
        this.getExcelAppender().append(data);
        return this;
    }

    @Override
    public BatchExcelWriter writeItem(SheetData<?> data, int startRowIndex) {
        this.getExcelAppender().append(data, startRowIndex);
        return this;
    }

    @Override
    public BatchExcelWriter writeItem(DataExtractor<?> dataExtractor, SheetData<?> data) {
        this.getExcelAppender().append(dataExtractor, data);
        return this;
    }

    @Override
    public BatchExcelWriter extractMode(DataExtractMode extractMode) {
        DataExtractor<?> dataExtractor = this.getDataExtractor();
        if (dataExtractor instanceof SwitchableDataExtractor) {
            ((SwitchableDataExtractor<?>) dataExtractor).extractMode(extractMode);
        }
        return this;
    }

}
