package com.github.aqiu202.excel.write;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public interface SheetExcelWriter extends SwitchableExcelWriter<SheetExcelWriter>,
        CombineExcelWriter<SheetExcelWriter> {

    <T> Workbook writeWorkbook(Class<T> type, Collection<T> data);
    <T> Workbook writeWorkbook(Class<T> type, Collection<T> data, int startRowIndex);

    default <T> void writeTo(Class<T> type, Collection<T> data, OutputStream os) {
        try {
            this.writeWorkbook(type, data).write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default <T> void writeTo(Class<T> type, Collection<T> data, File file) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            this.writeTo(type, data, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default <T> void writeTo(Class<T> type, Collection<T> data, String filePath) {
        this.writeTo(type, data, new File(filePath));
    }

}
