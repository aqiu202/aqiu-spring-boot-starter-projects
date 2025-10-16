package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;
import com.github.aqiu202.excel.read.convert.MappedCellValueConverter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface ExcelSheetReader<T> {

    int DEFAULT_HEAD_ROWS = 1;

    Class<T> getDataType();

    SheetReadConfiguration getConfiguration();

    ExcelSheetReader<T> configuration(SheetReadConfiguration configuration);

    ExcelSheetReader<T> configuration(Consumer<SheetReadConfiguration> configurationConsumer);

    ExcelSheetReader<T> converterFactory(ConverterFactory converterFactory);

    ExcelSheetReader<T> addListener(ReadDataListener<T> listener);

    ExcelSheetReader<T> rawDataFilter(ReadDataFilter<RowMappedCellValues> rawDataFilter);

    ExcelSheetReader<T> filter(ReadDataFilter<T> filter);

    ExcelSheetReader<T> setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter);

    ExcelSheetReader<T> setTypeTranslator(TypeTranslator typeTranslator);

    default List<T> read(Workbook workbook) {
        return this.read(workbook, 0, DEFAULT_HEAD_ROWS);
    }

    List<T> read(Workbook workbook, int sheetIndex, int headRows);

    List<T> read(Workbook workbook, String sheetName, int headRows);

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
        return this.read(is, sheetIndex, DEFAULT_HEAD_ROWS);
    }

    default List<T> read(File file, int sheetIndex) {
        return this.read(file, sheetIndex, DEFAULT_HEAD_ROWS);
    }

    default List<T> read(String filepath, int sheetIndex) {
        return this.read(filepath, sheetIndex, DEFAULT_HEAD_ROWS);
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

    default List<T> read(InputStream is, String sheetName) {
        return this.read(is, sheetName, 1);
    }

    default List<T> read(File file, String sheetName) {
        return this.read(file, sheetName, 1);
    }

    default List<T> read(String filepath, String sheetName) {
        return this.read(filepath, sheetName, 1);
    }

    default List<T> read(InputStream is, String sheetName, int headRows) {
        try {
            return this.read(WorkbookFactory.create(is), sheetName, headRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(File file, String sheetName, int headRows) {
        try {
            return this.read(new FileInputStream(file), sheetName, headRows);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(String filepath, String sheetName, int headRows) {
        return this.read(new File(filepath), sheetName, headRows);
    }

    default Map<String, List<T>> readAll(String filepath) {
        return this.readAll(filepath, DEFAULT_HEAD_ROWS);
    }

    default Map<String, List<T>> readAll(File file) {
        return this.readAll(file, DEFAULT_HEAD_ROWS);
    }

    default Map<String, List<T>> readAll(InputStream is) {
        return this.readAll(is, DEFAULT_HEAD_ROWS);
    }

    default Map<String, List<T>> readAll(Workbook workbook) {
        return this.readAll(workbook, DEFAULT_HEAD_ROWS);
    }

    default List<T>[] readAllWithIndex(String filepath) {
        return this.readAllWithIndex(filepath, DEFAULT_HEAD_ROWS);
    }

    default List<T>[] readAllWithIndex(File file) {
        return this.readAllWithIndex(file, DEFAULT_HEAD_ROWS);
    }

    default List<T>[] readAllWithIndex(InputStream is) {
        return this.readAllWithIndex(is, DEFAULT_HEAD_ROWS);
    }

    default List<T>[] readAllWithIndex(Workbook workbook) {
        return this.readAllWithIndex(workbook, DEFAULT_HEAD_ROWS);
    }

    default Map<String, List<T>> readAll(String filepath, int headRdRows) {
        return this.readAll(new File(filepath), headRdRows);
    }

    default Map<String, List<T>> readAll(File file, int headRdRows) {
        try {
            return this.readAll(new FileInputStream(file), headRdRows);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default Map<String, List<T>> readAll(InputStream is, int headRdRows) {
        try {
            return this.readAll(WorkbookFactory.create(is), headRdRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, List<T>> readAll(Workbook workbook, int headRdRows);

    default List<T>[] readAllWithIndex(String filepath, int headRdRows) {
        return this.readAllWithIndex(new File(filepath), headRdRows);
    }

    default List<T>[] readAllWithIndex(File file, int headRdRows) {
        try {
            return this.readAllWithIndex(new FileInputStream(file), headRdRows);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T>[] readAllWithIndex(InputStream is, int headRdRows) {
        try {
            return this.readAllWithIndex(WorkbookFactory.create(is), headRdRows);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<T>[] readAllWithIndex(Workbook workbook, int headRdRows);

    default List<Sheet> getAllSheets(Workbook workbook) {
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.add(workbook.getSheetAt(i));
        }
        return sheets;
    }

    default List<Sheet> getAllSheets(String filepath) {
        try {
            return this.getAllSheets(WorkbookFactory.create(new FileInputStream(filepath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default List<Sheet> getAllSheets(InputStream is) {
        try {
            return this.getAllSheets(WorkbookFactory.create(is));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
