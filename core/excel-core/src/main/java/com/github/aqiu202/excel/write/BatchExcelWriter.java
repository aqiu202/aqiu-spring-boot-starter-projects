package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.extract.DataExtractor;
import com.github.aqiu202.excel.model.SheetData;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

public interface BatchExcelWriter extends SwitchableExcelWriter<BatchExcelWriter>,
        CombineExcelWriter<BatchExcelWriter> {

    default Workbook write(SheetData<?>... dataArray) {
        return this.write(Arrays.asList(dataArray));
    }

    Workbook write(Collection<SheetData<?>> dataCollection);

    BatchExcelWriter writeItem(DataExtractor<?> dataExtractor, SheetData<?> data, int startRowIndex);
    BatchExcelWriter writeItem(DataExtractor<?> dataExtractor, SheetData<?> data);

    BatchExcelWriter writeItem(SheetData<?> data);
    BatchExcelWriter writeItem(SheetData<?> data, int startRowIndex);

    default void writeTo(SheetData<?> data, OutputStream os) {
        try {
            this.write(data).write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(Collection<SheetData<?>> data, OutputStream os) {
        try {
            this.write(data).write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(Collection<SheetData<?>> data, File file) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            this.writeTo(data, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeTo(Collection<SheetData<?>> data, String filePath) {
        this.writeTo(data, new File(filePath));
    }

}
