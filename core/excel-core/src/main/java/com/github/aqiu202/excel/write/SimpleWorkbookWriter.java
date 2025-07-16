package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.format.wrap.FormatedValueWrapper;
import com.github.aqiu202.excel.format.wrap.NumberValueWrapper;
import com.github.aqiu202.excel.format.wrap.StringValueWrapper;
import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.style.SimpleStyleProcessor;
import com.github.aqiu202.excel.style.StyleProcessor;
import com.github.aqiu202.excel.style.StyleProperty;
import com.github.aqiu202.excel.write.extract.DataExtractor;
import com.github.aqiu202.excel.write.hand.CellHandler;
import com.github.aqiu202.excel.write.hand.RowHandler;
import com.github.aqiu202.excel.write.hand.SheetHandler;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class SimpleWorkbookWriter implements WorkbookWriter {

    private final HandlerStore handlerStore = new SimpleHandlerStore();
    private ConverterFactory converterFactory;
    private CellValueSetter cellValueSetter = new SimpleCellValueSetter();
    private FormulaResolver formulaResolver = new SimpleFormulaResolver();

    public SimpleWorkbookWriter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    @Override
    public void setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public CellValueSetter getCellValueSetter() {
        return cellValueSetter;
    }

    @Override
    public void setCellValueSetter(CellValueSetter cellValueSetter) {
        this.cellValueSetter = cellValueSetter;
    }

    @Override
    public FormulaResolver getFormulaResolver() {
        return formulaResolver;
    }

    @Override
    public void setFormulaResolver(FormulaResolver formulaResolver) {
        this.formulaResolver = formulaResolver;
    }

    @Override
    public Workbook createWorkbook(WorkbookType type, int rowAccessWindowSize) {
        if (WorkbookType.SXSSF.equals(type)) {
            return ClassUtils.newInstance(type.getType(), new Class[]{int.class},
                    new Integer[]{rowAccessWindowSize});
        }
        return ClassUtils.newInstance(type.getType());
    }

    @Override
    public <T extends DataMeta> Sheet writeMetas(Workbook workbook, DataExtractor<T> dataExtractor,
                                                 String sheetName, Class<?> type, SheetWriteConfiguration configuration) {
        StyleProcessor styleProcessor = new SimpleStyleProcessor(workbook);
        int sheetIndex = workbook.getNumberOfSheets();
        Sheet sheet;
        if (ExcelSheetWriter.SHEET_NAME_PREFIX.equals(sheetName)) {
            int newSheetIndex = sheetIndex + 1;
            sheet = workbook.createSheet(sheetName + newSheetIndex);
        } else {
            sheet = workbook.createSheet(sheetName);
        }
        // 获取表元数据
        List<T> metaList = dataExtractor.extractMetas(type);
        // 从元数据中提取表头信息
        Heads heads = dataExtractor.extractHeadsFromMetas(metaList);
        List<HeadItem> headItems = heads.getAllHeadItems();
        RowHandler rowHandler = this.handlerStore.getResolvedRowHandler();
        CellHandler cellHandler = this.handlerStore.getResolvedCellHandler();
        int headRows = heads.getHeadRows();
        // 创建表头行
        for (int i = 0; i < headRows; i++) {
            Row headRow = sheet.getRow(i);
            if (headRow == null) {
                headRow = sheet.createRow(i);
            }
            rowHandler.handle(i, headRow, metaList, styleProcessor, configuration);
        }
        // 填写表头内容
        for (HeadItem headItem : headItems) {
            String title = headItem.getContent();
            CellIndex index = headItem.getIndex();
            int rowIndex = index.getRowIndex();
            int colIndex = index.getColIndex();
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(title);
            // 设置表头样式
            this.setCellStyle(cell, styleProcessor, configuration.getHeadStyle());
            cellHandler.handleHead(rowIndex, colIndex, cell, new StringValueWrapper(title), styleProcessor, configuration);
            if (headItem instanceof MergedHeadItem) {
                MergedHeadItem mergedHeadItem = (MergedHeadItem) headItem;
                int colspan = mergedHeadItem.getColspan();
                boolean repeatContent = mergedHeadItem.isRepeatContent();
                // 创建空白的单元格，并设置格式，否则不显示边框
                if (colspan > 1) {
                    for (int i = 1; i <= colspan - 1; i++) {
                        int blankColIndex = colIndex + i;
                        Cell blankCell = row.createCell(blankColIndex);
                        if (repeatContent) {
                            blankCell.setCellValue(title);
                        }
                        this.setCellStyle(blankCell, styleProcessor, configuration.getHeadStyle());
                        cellHandler.handleHead(rowIndex, blankColIndex, blankCell, new StringValueWrapper(title), styleProcessor, configuration);
                    }
                }
                CellRangeAddress cellAddresses = new CellRangeAddress(rowIndex, rowIndex + mergedHeadItem.getRowspan() - 1,
                        colIndex, colIndex + mergedHeadItem.getColspan() - 1);
                sheet.addMergedRegion(cellAddresses);
            }
        }
        return sheet;
    }

    @Override
    public <M extends DataMeta, D> void appendData(Sheet sheet, DataExtractor<M> dataExtractor,
                                                   Class<D> dataType, Collection<D> rows, SheetWriteConfiguration configuration) {
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }
        StyleProcessor styleProcessor = new SimpleStyleProcessor(sheet.getWorkbook());
        int contentRowIndex = sheet.getLastRowNum() + 1;
        RowHandler rowHandler = this.handlerStore.getResolvedRowHandler();
        CellHandler cellHandler = this.handlerStore.getResolvedCellHandler();
        List<M> metaList = dataExtractor.extractMetas(dataType);
        int columnCount = metaList.size();
        for (D item : rows) {
            Row row = sheet.createRow(contentRowIndex);
            rowHandler.handle(contentRowIndex, row, item, styleProcessor, configuration);
            for (int j = 0; j < columnCount; j++) {
                Cell cell = row.createCell(j);
                M meta = metaList.get(j);
                String formula = meta.getFormula();
                // 如果配置了公式，则只进行设置公式即可，无需赋值
                if (StringUtils.isNotBlank(formula)) {
                    String fv = this.formulaResolver.resolve(formula, row.getRowNum(), metaList);
                    cell.setCellFormula(fv);
                    this.configureContentCell(cell, null, styleProcessor, configuration.getContentStyle());
                    cellHandler.handleContent(contentRowIndex, j, cell, null, styleProcessor, configuration);
                    continue;
                }
                ValueWrapper<?> value = dataExtractor.extractFormattedValue(meta, item, configuration, this.getConverterFactory());
                this.configureContentCell(cell, value, styleProcessor, configuration.getContentStyle());
                cellHandler.handleContent(contentRowIndex, j, cell, value, styleProcessor, configuration);
                this.cellValueSetter.setCellValue(cell, value, styleProcessor, configuration);
            }
            contentRowIndex++;
        }

        this.configureSheet(sheet, metaList, configuration);
        SheetHandler sheetHandler = this.handlerStore.getResolvedSheetHandler();
        sheetHandler.handle(sheet, columnCount, rows, configuration);
    }

    /**
     * 为给定的单元格设置样式。
     * 该方法通过提供的样式处理器和样式属性，构建并应用一个单元格样式。
     *
     * @param cell           需要设置样式的单元格。
     * @param styleProcessor 样式处理器，负责根据样式属性构建单元格样式。
     * @param property       样式属性，定义了单元格样式的具体属性，如颜色、字体等。
     */
    protected void setCellStyle(Cell cell, StyleProcessor styleProcessor, StyleProperty property) {
        CellStyle cellStyle = styleProcessor.buildCellStyle(property);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 配置单元格样式
     *
     * @param cell           单元格
     * @param result         值
     * @param styleProcessor 样式处理器
     * @param property       样式
     */
    protected void configureContentCell(Cell cell, ValueWrapper<?> result, StyleProcessor styleProcessor, StyleProperty property) {
        this.setCellStyle(cell, styleProcessor, property);
        // 与前端统一风格，数值型右对齐
        if (result instanceof NumberValueWrapper) {
            styleProcessor.modifyCellStyle(cell,
                    p -> p.setAlignment(HorizontalAlignment.RIGHT));
        }
        if (result instanceof FormatedValueWrapper) {
            FormatedValueWrapper frw = (FormatedValueWrapper) result;
            ValueWrapper<?> original = frw.getOriginal();
            if (original instanceof NumberValueWrapper) {
                styleProcessor.modifyCellStyle(cell,
                        p -> p.setAlignment(HorizontalAlignment.RIGHT));
            }
        }
    }

    /**
     * 配置sheet
     *
     * @param sheet         sheet页
     * @param metaList      原数据描述集合
     * @param configuration 配置
     */
    protected <M extends DataMeta> void configureSheet(Sheet sheet, List<M> metaList, SheetWriteConfiguration configuration) {
        int columnCount = metaList.size();
        if (configuration.isAutoSizeColumn()) {
            // SXSSFSheet设置列宽自适应之前需要启用trackAllColumnsForAutoSizing
            if (sheet instanceof SXSSFSheet) {
                ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            }
            // 设置列宽自适应比例
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
                double autoWidthRatio = configuration.getAutoWidthRatio();
                if (autoWidthRatio != 1) {
                    int width = BigDecimal.valueOf(sheet.getColumnWidth(i) * configuration.getAutoWidthRatio()).intValue();
                    sheet.setColumnWidth(i, width);
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                M meta = metaList.get(i);
                int width = meta.getWidth();
                if (width > 0) {
                    sheet.setColumnWidth(i, width * 256);
                } else {
                    sheet.autoSizeColumn(i);
                }
            }
        }
    }

    /**
     * 添加/取消保护sheet
     *
     * @param sheet             sheet页
     * @param protectedPassword 密码（为空时去除保护）
     */
    @Override
    public void processSheet(Sheet sheet, String protectedPassword) {
        if (StringUtils.isBlank(protectedPassword)) {
            protectedPassword = null;
        }
        sheet.protectSheet(protectedPassword);
    }

    @Nonnull
    @Override
    public SheetHandler getResolvedSheetHandler() {
        return this.handlerStore.getResolvedSheetHandler();
    }

    @Nonnull
    @Override
    public RowHandler getResolvedRowHandler() {
        return this.handlerStore.getResolvedRowHandler();
    }

    @Nonnull
    @Override
    public CellHandler getResolvedCellHandler() {
        return this.handlerStore.getResolvedCellHandler();
    }

    @Override
    public void addSheetHandler(SheetHandler sheetHandler) {
        this.handlerStore.addSheetHandler(sheetHandler);
    }

    @Override
    public void addRowHandler(RowHandler rowHandler) {
        this.handlerStore.addRowHandler(rowHandler);
    }

    @Override
    public void addCellHandler(CellHandler cellHandler) {
        this.handlerStore.addCellHandler(cellHandler);
    }
}
