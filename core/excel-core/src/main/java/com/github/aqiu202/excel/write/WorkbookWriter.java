package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

public interface WorkbookWriter extends HandlerStore {

    default Workbook createWorkbook() {
        return this.createWorkbook(WorkbookType.SXSSF);
    }

    void setConverterFactory(ConverterFactory converterFactory);

    ConverterFactory getConverterFactory();

    void setCellValueSetter(CellValueSetter cellValueSetter);

    CellValueSetter getCellValueSetter();

    void setFormulaResolver(FormulaResolver formulaResolver);

    FormulaResolver getFormulaResolver();

    Workbook createWorkbook(WorkbookType type, int rowAccessWindowSize);

    default Workbook createWorkbook(WorkbookType type) {
        return this.createWorkbook(type, WorkbookSheetWriteConfiguration.DEFAULT_ROW_ACCESS_WINDOW_SIZE);
    }

    <T extends TableMeta> Sheet writeMetas(Workbook workbook, DataExtractor<T> dataExtractor, String sheetName, Class<?> type, SheetWriteConfiguration configuration);

    <T extends TableMeta, D> void appendData(Sheet sheet, DataExtractor<T> dataExtractor, Class<D> dataType, Collection<D> rows, SheetWriteConfiguration configuration);

    void processSheet(Sheet sheet, String protectedPassword);

}
