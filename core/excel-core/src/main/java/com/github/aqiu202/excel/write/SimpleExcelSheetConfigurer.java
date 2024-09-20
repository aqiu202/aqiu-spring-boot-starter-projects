package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.hand.CellHandler;
import com.github.aqiu202.excel.write.hand.RowHandler;
import com.github.aqiu202.excel.write.hand.SheetHandler;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.Consumer;

public class SimpleExcelSheetConfigurer<T> implements ExcelSheetConfigurer<T> {

    private final DataExtractor<?> dataExtractor;
    private final Class<T> dataType;
    private String sheetName = ExcelSheetWriter.SHEET_NAME_PREFIX;
    private final ConverterFactory writerConverterFactory;
    private final WorkbookSheetWriteConfiguration writerConfiguration;
    private SheetWriteConfiguration configuration;
    private final WorkbookWriter workbookWriter;
    private String protectedPassword;
    private final Workbook workbook;

    public SimpleExcelSheetConfigurer(DataExtractor<?> dataExtractor, Class<T> dataType, ExcelSheetWriter<?> sheetWriter) {
        this(dataExtractor, dataType, sheetWriter.getConfigurer().getWriterConverterFactory(),
                sheetWriter.getConfigurer().getWriterConfiguration(), sheetWriter.getWorkbook());
    }

    public SimpleExcelSheetConfigurer(DataExtractor<?> dataExtractor, Class<T> dataType,
                                      ConverterFactory converterFactory,
                                      WorkbookSheetWriteConfiguration configuration) {
        this(dataExtractor, dataType, converterFactory, configuration, null);
    }

    public SimpleExcelSheetConfigurer(DataExtractor<?> dataExtractor, Class<T> dataType,
                                      ConverterFactory converterFactory,
                                      WorkbookSheetWriteConfiguration configuration, Workbook workbook) {
        this.dataExtractor = dataExtractor;
        this.dataType = dataType;
        this.writerConverterFactory = converterFactory;
        this.writerConfiguration = configuration;
        this.workbookWriter = new SimpleWorkbookWriter(converterFactory);
        this.workbook = workbook;
    }

    @Override
    public Class<T> getDataType() {
        return this.dataType;
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    @Override
    public String getProtectedPassword() {
        return protectedPassword;
    }

    @Override
    public WorkbookSheetWriteConfiguration getWriterConfiguration() {
        return this.writerConfiguration;
    }

    @Override
    public ConverterFactory getWriterConverterFactory() {
        return this.writerConverterFactory;
    }

    @Override
    public DataExtractor<?> getDataExtractor() {
        return this.dataExtractor;
    }

    @Override
    public WorkbookWriter getWorkbookWriter() {
        return this.workbookWriter;
    }

    @Override
    public ExcelSheetConfigurer<T> converterFactory(ConverterFactory converterFactory) {
        this.getWorkbookWriter().setConverterFactory(converterFactory);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> cellValueSetter(CellValueSetter cellValueSetter) {
        this.getWorkbookWriter().setCellValueSetter(cellValueSetter);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> formulaResolver(FormulaResolver formulaResolver) {
        this.getWorkbookWriter().setFormulaResolver(formulaResolver);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> protectSheet(String password) {
        this.protectedPassword = password;
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> configuration(SheetWriteConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> configuration(Consumer<SheetWriteConfiguration> configurationConsumer) {
        if (this.configuration == null) {
            this.configuration = new SheetWriteConfiguration();
        }
        configurationConsumer.accept(this.configuration);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> addSheetHandler(SheetHandler sheetHandler) {
        this.getWorkbookWriter().addSheetHandler(sheetHandler);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> addRowHandler(RowHandler rowHandler) {
        this.getWorkbookWriter().addRowHandler(rowHandler);
        return this;
    }

    @Override
    public ExcelSheetConfigurer<T> addCellHandler(CellHandler cellHandler) {
        this.getWorkbookWriter().addCellHandler(cellHandler);
        return this;
    }

    @Override
    public ExcelSheetWriter<T> then() {
        return new SimpleExcelSheetWriter<>(this);
    }

    @Override
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * 获取配置信息（本身配置优先于ExcelWriter的配置）
     *
     * @return 配置信息
     */
    @Override
    public SheetWriteConfiguration getResolvedConfiguration() {
        SheetWriteConfiguration configuration = this.configuration;
        if (configuration == null) {
            configuration = this.getWriterConfiguration();
        }
        return configuration;
    }
}
