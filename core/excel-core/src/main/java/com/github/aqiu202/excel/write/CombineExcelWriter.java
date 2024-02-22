package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.appender.ExcelAppender;
import com.github.aqiu202.excel.convert.ConverterFactoryWrapper;
import com.github.aqiu202.excel.extract.DataExtractorWrapper;
import com.github.aqiu202.excel.model.SheetData;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public interface CombineExcelWriter<T extends CombineExcelWriter<?>> extends ConfigurableExcelWriter<T>,
        DataExtractorWrapper<T>, ConverterFactoryWrapper<T> {

    ExcelAppender getExcelAppender();

    default Workbook getWorkbook() {
        return this.getExcelAppender().getWorkbook();
    }

    default T writeItem(SheetData<?> data) {
        this.getExcelAppender().append(data);
        return (T) this;
    }

    default T writeItem(SheetData<?> data, int startRowIndex) {
        this.getExcelAppender().append(data, startRowIndex);
        return (T) this;
    }

    default void writeTo(SheetData<?> data, OutputStream os) {
        try {
            ExcelAppender excelAppender = this.getExcelAppender();
            excelAppender.append(data).getWorkbook().write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(Collection<SheetData<?>> data, OutputStream os) {
        try {
            ExcelAppender excelAppender = this.getExcelAppender();
            excelAppender.append(data).getWorkbook().write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(OutputStream os) {
        try {
            this.getWorkbook().write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(File file) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            this.writeTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(String filePath) {
        this.writeTo(new File(filePath));
    }

}
