package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.ReadConfiguration;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;
import com.github.aqiu202.excel.read.convert.MappedCellValueConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.List;
import java.util.function.Consumer;

public interface ExcelSheetReader<T> {

    Class<T> getDataType();

    ReadConfiguration getConfiguration();

    ExcelSheetReader<T> configuration(ReadConfiguration configuration);

    ExcelSheetReader<T> configuration(Consumer<ReadConfiguration> configurationConsumer);

    ExcelSheetReader<T> converterFactory(ConverterFactory converterFactory);

    ExcelSheetReader<T> addListener(ReadDataListener<T> listener);

    ExcelSheetReader<T> rawDataFilter(ReadDataFilter<RowMappedCellValues> rawDataFilter);

    ExcelSheetReader<T> filter(ReadDataFilter<T> filter);

    ExcelSheetReader<T> setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter);

    ExcelSheetReader<T> setTypeTranslator(TypeTranslator typeTranslator);

    default List<T> read(Workbook workbook) {
        return this.read(workbook, 0, 1);
    }

    List<T> read(Workbook workbook, int sheetIndex, int headRows);

    default List<T> read(InputStream is) {
        return this.read(is, 0);
    }

    default List<T> read(File file) {
        try {
            return this.read(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(String filepath) {
        return this.read(new File(filepath));
    }

    default List<T> read(InputStream is, int sheetIndex) {
        return this.read(is, sheetIndex, 1);
    }

    default List<T> read(File file, int sheetIndex) {
        return this.read(file, sheetIndex, 1);
    }

    default List<T> read(String filepath, int sheetIndex) {
        return this.read(filepath, sheetIndex, 1);
    }

    default List<T> read(InputStream is, int sheetIndex, int headRows) {
        try {
            return this.read(WorkbookFactory.create(is), sheetIndex, headRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(File file, int sheetIndex, int headRows) {
        try {
            return this.read(new FileInputStream(file), sheetIndex, headRows);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(String filepath, int sheetIndex, int headRows) {
        return this.read(new File(filepath), sheetIndex, headRows);
    }

}
