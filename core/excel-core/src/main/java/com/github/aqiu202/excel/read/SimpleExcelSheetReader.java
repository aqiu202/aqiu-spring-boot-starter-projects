package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.Converter;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.ConverterProvider;
import com.github.aqiu202.excel.convert.ConverterProviderWrapper;
import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.excel.read.convert.MappedCellValueConverter;
import com.github.aqiu202.excel.read.convert.RowMappedCellValuesConverter;
import com.github.aqiu202.excel.read.convert.SimpleRowMappedCellValuesConverter;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleExcelSheetReader<T> implements ExcelSheetReader<T> {
    protected final RowMappedCellValuesConverter rowMappedCellValuesConverter = new SimpleRowMappedCellValuesConverter();
    protected final List<ReadDataListener<T>> listeners = new ArrayList<>();
    private final Class<T> type;
    protected final DataAnalyser dataAnalyser;
    protected TypeTranslator typeTranslator = new SimpleTypeTranslator();
    private ReadDataFilter<RowMappedCellValues> rawDataFilter;
    private ReadDataFilter<T> filter;
    private SheetReadConfiguration configuration;
    private ConverterFactory converterFactory;
    private final ExcelBeforeReadHandler beforeReadHandler;

    public SimpleExcelSheetReader(Class<T> type, MetaAnalyzer<?> metaAnalyzer, ConverterFactory converterFactory,
                                  SheetReadConfiguration configuration, ExcelBeforeReadHandler beforeReadHandler) {
        this(type, new SimpleDataAnalyser(metaAnalyzer), converterFactory, configuration, beforeReadHandler);
    }

    public SimpleExcelSheetReader(Class<T> type, DataAnalyser dataAnalyser, ConverterFactory converterFactory,
                                  SheetReadConfiguration configuration, ExcelBeforeReadHandler beforeReadHandler) {
        if (ClassUtils.isCustomClass(type) || ClassUtils.isAssignableFrom(Map.class, type)) {
            this.type = type;
        } else {
            throw new RuntimeException("不支持的类型");
        }
        this.dataAnalyser = dataAnalyser;
        this.converterFactory = converterFactory;
        this.configuration = configuration;
        this.beforeReadHandler = beforeReadHandler;
    }

    @Override
    public Class<T> getDataType() {
        return this.type;
    }

    @Override
    public SheetReadConfiguration getConfiguration() {
        return configuration;
    }

    public MappedCellValueConverter getMappedCellValueConverter() {
        return this.rowMappedCellValuesConverter.getMappedCellValueConverter();
    }

    public RowMappedCellValuesConverter getRowMappedCellValuesConverter() {
        return rowMappedCellValuesConverter;
    }

    @Override
    public SimpleExcelSheetReader<T> setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter) {
        this.rowMappedCellValuesConverter.setMappedCellValueConverter(mappedCellValueConverter);
        return this;
    }

    public TypeTranslator getTypeTranslator() {
        return typeTranslator;
    }

    @Override
    public SimpleExcelSheetReader<T> setTypeTranslator(TypeTranslator typeTranslator) {
        this.typeTranslator = typeTranslator;
        return this;
    }

    @Override
    public SimpleExcelSheetReader<T> configuration(SheetReadConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public ExcelSheetReader<T> configuration(Consumer<SheetReadConfiguration> configurationConsumer) {
        if (this.configuration == null) {
            this.configuration = new SheetReadConfiguration();
        }
        configurationConsumer.accept(this.configuration);
        return this;
    }

    @Override
    public ExcelSheetReader<T> converterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

    @Override
    public ExcelSheetReader<T> addListener(ReadDataListener<T> listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public ExcelSheetReader<T> filter(ReadDataFilter<T> filter) {
        this.filter = filter;
        return this;
    }

    public ReadDataFilter<RowMappedCellValues> getRawDataFilter() {
        return rawDataFilter;
    }

    @Override
    public SimpleExcelSheetReader<T> rawDataFilter(ReadDataFilter<RowMappedCellValues> rawDataFilter) {
        this.rawDataFilter = rawDataFilter;
        return this;
    }

    public List<ReadDataListener<T>> getListeners() {
        return listeners;
    }

    public ReadDataFilter<T> getFilter() {
        return filter;
    }

    @Override
    public List<T> read(InputStream is, int sheetIndex, int headRows) {
        return ExcelSheetReader.super.read(this.handleInputStream(is), sheetIndex, headRows);
    }

    protected InputStream handleInputStream(InputStream is) {
        InputStream targetInputStream = is;
        if (beforeReadHandler != null) {
            try {
                targetInputStream = beforeReadHandler.handle(is);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return targetInputStream;
    }

    @Override
    public Map<String, List<T>> readAll(InputStream is, int headRdRows) {
        return ExcelSheetReader.super.readAll(this.handleInputStream(is), headRdRows);
    }

    @Override
    public List<T>[] readAllWithIndex(InputStream is, int headRdRows) {
        return ExcelSheetReader.super.readAllWithIndex(this.handleInputStream(is), headRdRows);
    }

    @Override
    public List<T> read(Workbook workbook, int sheetIndex, int headRows) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new RuntimeException("Sheet页不存在");
        }
        return this.readSheet(sheet, headRows);
    }

    @Override
    public List<T> read(Workbook workbook, String sheetName, int headRows) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException(String.format("Sheet[%s]页不存在", sheetName));
        }
        return this.readSheet(sheet, headRows);
    }

    protected List<T> readSheet(Sheet sheet, int headRows) {
        List<RowMappedCellValues> rowMappedCellValues = this.readSheetRows(sheet, headRows);
        if (CollectionUtils.isEmpty(rowMappedCellValues)) {
            return Collections.emptyList();
        }
        return rowMappedCellValues.stream()
                .peek(values -> this.getRowMappedCellValuesConverter().convert(values, configuration))
                .filter(item -> this.getRawDataFilter() == null || this.getRawDataFilter().test(item))
                .map(values -> this.getTypeTranslator().translate(values, type))
                .filter(item -> this.getFilter() == null || this.getFilter().test(item))
                .peek(item -> this.getListeners().forEach(listener -> listener.onData(item))).collect(Collectors.toList());
    }

    protected List<RowMappedCellValues> readSheetRows(Sheet sheet, int headRows) {
        Class<T> type = this.getDataType();
        SheetReadConfiguration configuration = this.getConfiguration();
        int minRowNum = sheet.getFirstRowNum();
        int maxRowNum = sheet.getLastRowNum();
        // 获取表内容第一行
        int contentFirstRowNum = minRowNum + headRows;
        // 获取表头末行(表内容前一行)
        Row headRow = sheet.getRow(contentFirstRowNum - 1);
        int startColIndex = headRow.getFirstCellNum();
        int columns = headRow.getPhysicalNumberOfCells();
        List<IndexedTableMeta> indexedTableMetas = this.dataAnalyser.analyse(sheet, type, startColIndex, columns, headRows);
        List<RowMappedCellValues> mappedCellValues = new ArrayList<>();
        for (int i = contentFirstRowNum; i <= maxRowNum; i++) {
            Row row = sheet.getRow(i);
            RowMappedCellValues rowCellValues = new SimpleRowMappedCellValues(i, columns);
            for (IndexedTableMeta indexedTableMeta : indexedTableMetas) {
                int colIndex = indexedTableMeta.getIndex();
                Cell cell = row.getCell(colIndex);
                CellVal<?> cellVal = this.dataAnalyser.readConvertedCellValue(cell,
                        configuration, this.findConverter(indexedTableMeta.getMeta()));
                // 空/未知数据跳过不处理
                if (cellVal instanceof NullCellVal || cellVal instanceof BlankCellVal
                        || cellVal instanceof UnknownCellVal) {
                    continue;
                }
                rowCellValues.setMappedCellValue(colIndex, new SimpleMappedCellValue(indexedTableMeta, cellVal));
            }
            mappedCellValues.add(rowCellValues);
        }
        return mappedCellValues;
    }

    @Override
    public Map<String, List<T>> readAll(Workbook workbook, int headRdRows) {
        int numberOfSheets = workbook.getNumberOfSheets();
        Map<String, List<T>> result = new HashMap<>();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            result.put(sheet.getSheetName(), this.read(workbook, i, headRdRows));
        }
        return result;
    }

    @Override
    public List<T>[] readAllWithIndex(Workbook workbook, int headRdRows) {
        int numberOfSheets = workbook.getNumberOfSheets();
        List<T>[] result = new ArrayList[numberOfSheets];
        for (int i = 0; i < numberOfSheets; i++) {
            result[i] = this.read(workbook, i, headRdRows);
        }
        return result;
    }

    /**
     * 获取转换器提供者
     *
     * @param tableMeta 表元数据
     * @return 转换器提供者
     */
    protected ConverterProvider findConverterProvider(TableMeta tableMeta) {
        if (tableMeta instanceof ConverterProviderWrapper) {
            ConverterProviderWrapper wrapper = (ConverterProviderWrapper) tableMeta;
            return wrapper.getConverterProvider();
        }
        return null;
    }

    /**
     * 获取转换器
     *
     * @param tableMeta 表元数据
     * @return 转换器
     */
    protected Converter<?, ?> findConverter(TableMeta tableMeta) {
        ConverterProvider converterProvider = this.findConverterProvider(tableMeta);
        if (converterProvider != null) {
            return this.converterFactory.findConverter(converterProvider.getConverter());
        }
        return null;
    }

}
