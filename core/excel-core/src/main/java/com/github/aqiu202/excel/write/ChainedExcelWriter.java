package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.write.extract.AnnotationDataExtractor;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.extract.TypeDataExtractor;
import org.apache.poi.ss.usermodel.Workbook;

public class ChainedExcelWriter implements ExcelWriter, ExportableWorkbookWrapper {

    private final Workbook workbook;
    private final ExcelSheetWriter<?> sheetWriter;

    public ChainedExcelWriter(ExcelSheetWriter<?> sheetWriter) {
        this.sheetWriter = sheetWriter;
        this.workbook = sheetWriter.getWorkbook();
    }

    @Override
    public Workbook getWorkbook() {
        return this.workbook;
    }

    @Override
    public <T> ExcelSheetConfigurer<T> type(Class<T> type) {
        return new SimpleExcelSheetConfigurer<>(TypeDataExtractor.INSTANCE, type, this.sheetWriter);
    }

    @Override
    public <T> AnnotationExcelSheetConfigurer<T> annotation(Class<T> type) {
        if (type.isInterface()) {
            throw new RuntimeException("暂不支持接口类型");
        }
        return new AnnotationExcelSheetConfigurer<>(new AnnotationDataExtractor(), type, this.sheetWriter);
    }

    @Override
    public <T> ExcelSheetConfigurer<T> custom(DataExtractor<?> dataExtractor, Class<T> type) {
        return new SimpleExcelSheetConfigurer<>(dataExtractor, type, this.sheetWriter);
    }

    @Override
    public ExcelBeforeExportHandler getBeforeExportHandler() {
        return this.sheetWriter.getBeforeExportHandler();
    }
}
