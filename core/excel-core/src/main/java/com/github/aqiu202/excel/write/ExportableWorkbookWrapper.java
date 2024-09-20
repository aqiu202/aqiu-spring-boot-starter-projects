package com.github.aqiu202.excel.write;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

public interface ExportableWorkbookWrapper extends Exportable, WorkbookWrapper {

    @Override
    default void exportTo(OutputStream os) {
        Workbook workbook = this.getWorkbook();
        if (workbook != null) {
            try {
                workbook.write(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
