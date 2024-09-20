package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.hand.CellHandler;
import com.github.aqiu202.excel.write.hand.RowHandler;
import com.github.aqiu202.excel.write.hand.SheetHandler;

import java.util.function.Consumer;

public interface ExcelSheetConfigurer<T> extends WorkbookWrapper {

    Class<T> getDataType();

    String getSheetName();

    String getProtectedPassword();

    WorkbookSheetWriteConfiguration getWriterConfiguration();

    ConverterFactory getWriterConverterFactory();

    DataExtractor<?> getDataExtractor();

    WorkbookWriter getWorkbookWriter();

    ExcelSheetConfigurer<T> converterFactory(ConverterFactory converterFactory);

    ExcelSheetConfigurer<T> cellValueSetter(CellValueSetter cellValueSetter);

    ExcelSheetConfigurer<T> formulaResolver(FormulaResolver formulaResolver);

    ExcelSheetConfigurer<T> sheetName(String sheetName);

    ExcelSheetConfigurer<T> protectSheet(String password);

    ExcelSheetConfigurer<T> configuration(SheetWriteConfiguration configuration);

    ExcelSheetConfigurer<T> configuration(Consumer<SheetWriteConfiguration> configurationConsumer);

    ExcelSheetConfigurer<T> addSheetHandler(SheetHandler sheetHandler);

    ExcelSheetConfigurer<T> addRowHandler(RowHandler rowHandler);

    ExcelSheetConfigurer<T> addCellHandler(CellHandler cellHandler);

    ExcelSheetWriter<T> then();

    SheetWriteConfiguration getResolvedConfiguration();
}
