package com.github.aqiu202.excel.write;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface ExportableWorkbookWrapper extends Exportable, WorkbookWrapper {


    ExcelBeforeExportHandler getBeforeExportHandler();

    @Override
    default void exportTo(OutputStream os) {
        Workbook workbook = this.getWorkbook();
        if (workbook != null) {
            try {
                ExcelBeforeExportHandler beforeExportHandler = this.getBeforeExportHandler();
                if (beforeExportHandler != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    workbook.write(out);
                    OutputStream targetOutputStream;
                    try {
                        targetOutputStream = beforeExportHandler.handle(os);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    out.writeTo(targetOutputStream);
                } else {
                    workbook.write(os);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
