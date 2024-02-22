package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.cell.MappedCellValueConverter;
import com.github.aqiu202.excel.mapping.ValueMapping;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.List;

public interface CommonExcelReader<T> {
    CommonExcelReader<T> addListener(ReadDataListener<T> listener);

    CommonExcelReader<T> rawDataFilter(ReadDataFilter<RowMappedCellValues> rawDataFilter);

    CommonExcelReader<T> filter(ReadDataFilter<T> filter);

    CommonExcelReader<T> valueMapping(ValueMapping<?> valueMapping);

    CommonExcelReader<T> setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter);

    CommonExcelReader<T> setTypeTranslator(TypeTranslator typeTranslator);

    default List<T> read(Workbook workbook) {
        return this.read(workbook, 0);
    }

    List<T> read(Workbook workbook, int sheetIndex);

    default List<T> read(InputStream is) {
        try {
            return this.read(WorkbookFactory.create(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return this.read(WorkbookFactory.create(is), sheetIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(File file, int sheetIndex) {
        try {
            return this.read(new FileInputStream(file), sheetIndex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    default List<T> read(String filepath, int sheetIndex) {
        return this.read(new File(filepath), sheetIndex);
    }

}
