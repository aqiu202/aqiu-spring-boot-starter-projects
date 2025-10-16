package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

public class SimpleExcelSheetWriter<T> implements ExcelSheetWriter<T> {

    private final ExcelSheetConfigurer<T> configurer;
    private Workbook workbook;
    private Sheet sheet;

    public SimpleExcelSheetWriter(ExcelSheetConfigurer<T> configurer) {
        this.configurer = configurer;
        this.workbook = configurer.getWorkbook();
    }

    @Override
    public ExcelSheetConfigurer<T> getConfigurer() {
        return this.configurer;
    }

    @Override
    public ExcelBeforeExportHandler getBeforeExportHandler() {
        return this.configurer.getBeforeExportHandler();
    }

    @Override
    public ExcelSheetWriter<T> write(Collection<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            return this;
        }
        ExcelSheetConfigurer<T> configurer = this.getConfigurer();
        WorkbookWriter writer = configurer.getWorkbookWriter();
        Class<T> dataType = configurer.getDataType();
        DataExtractor<?> dataExtractor = configurer.getDataExtractor();
        SheetWriteConfiguration configuration = configurer.getResolvedConfiguration();
        if (this.sheet == null) {
            this.sheet = writer.writeMetas(this.getWorkbook(), dataExtractor,
                    configurer.getSheetName(), dataType, configuration);
            String protectedPassword = configurer.getProtectedPassword();
            if (StringUtils.isNotBlank(protectedPassword)) {
                writer.processSheet(this.sheet, protectedPassword);
            }
        }
        writer.appendData(this.sheet, dataExtractor, dataType, data, configuration);
        return this;
    }

    @Override
    public ChainedExcelWriter next() {
        return new ChainedExcelWriter(this);
    }

    @Override
    public <S> ExcelSheetConfigurer<S> nextSheet(Class<S> type) {
        return this.next().custom(this.getConfigurer().getDataExtractor(), type);
    }

    @Override
    public Workbook getWorkbook() {
        if (this.workbook == null) {
            this.workbook = this.createWorkbook();
        }
        return this.workbook;
    }

    protected Workbook createWorkbook() {
        ExcelSheetConfigurer<T> configurer = this.getConfigurer();
        WorkbookSheetWriteConfiguration wwc = configurer.getWriterConfiguration();
        WorkbookWriter wbw = configurer.getWorkbookWriter();
        if (wwc != null) {
            return wbw.createWorkbook(
                    wwc.getWorkBookType(), wwc.getRowAccessWindowSize()
            );
        }
        return wbw.createWorkbook();
    }

}
