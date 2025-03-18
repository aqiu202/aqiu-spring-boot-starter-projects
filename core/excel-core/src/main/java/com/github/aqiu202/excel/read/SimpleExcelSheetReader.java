package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.Converter;
import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.ConverterProvider;
import com.github.aqiu202.excel.convert.ConverterProviderWrapper;
import com.github.aqiu202.excel.meta.IndexedMeta;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.excel.read.convert.MappedCellValueConverter;
import com.github.aqiu202.excel.read.convert.RowMappedCellValuesConverter;
import com.github.aqiu202.excel.read.convert.SimpleRowMappedCellValuesConverter;
import com.github.aqiu202.util.ClassUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleExcelSheetReader<T> implements ExcelSheetReader<T> {
    private final RowMappedCellValuesConverter rowMappedCellValuesConverter = new SimpleRowMappedCellValuesConverter();
    private final List<ReadDataListener<T>> listeners = new ArrayList<>();
    private final Class<T> type;
    private final DataAnalyser dataAnalyser;
    private TypeTranslator typeTranslator = new SimpleTypeTranslator();
    private ReadDataFilter<RowMappedCellValues> rawDataFilter;
    private ReadDataFilter<T> filter;
    private SheetReadConfiguration configuration;
    private ConverterFactory converterFactory;

    public SimpleExcelSheetReader(Class<T> type, MetaAnalyzer<?> metaAnalyzer, ConverterFactory converterFactory, SheetReadConfiguration configuration) {
        if (ClassUtils.isCustomClass(type) || ClassUtils.isAssignableFrom(Map.class, type)) {
            this.type = type;
        } else {
            throw new RuntimeException("不支持的类型");
        }
        this.dataAnalyser = new SimpleDataAnalyser(metaAnalyzer);
        this.converterFactory = converterFactory;
        this.configuration = configuration;
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
    public List<T> read(Workbook workbook, int sheetIndex, int headRows) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new RuntimeException("Sheet页不存在");
        }
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
        List<IndexedMeta> indexedMetas = this.dataAnalyser.analyse(sheet, type, startColIndex, columns, headRows);
        List<RowMappedCellValues> mappedCellValues = new ArrayList<>();
        for (int i = contentFirstRowNum; i <= maxRowNum; i++) {
            Row row = sheet.getRow(i);
            RowMappedCellValues rowCellValues = new SimpleRowMappedCellValues(i, columns);
            for (IndexedMeta indexedMeta : indexedMetas) {
                int colIndex = indexedMeta.getIndex();
                Cell cell = row.getCell(colIndex);
                CellVal<?> cellVal = this.dataAnalyser.readConvertedCellValue(cell,
                        configuration, this.findConverter(indexedMeta.getMeta()));
                // 空/未知数据跳过不处理
                if (cellVal instanceof NullCellVal || cellVal instanceof BlankCellVal
                        || cellVal instanceof UnknownCellVal) {
                    continue;
                }
                rowCellValues.setMappedCellValue(colIndex, new SimpleMappedCellValue(indexedMeta, cellVal));
            }
            mappedCellValues.add(rowCellValues);
        }
        return mappedCellValues.stream()
                .peek(values -> this.rowMappedCellValuesConverter.convert(values, configuration))
                .filter(item -> this.getRawDataFilter() == null || this.getRawDataFilter().test(item))
                .map(values -> this.getTypeTranslator().translate(values, type))
                .filter(item -> this.getFilter() == null || this.getFilter().test(item))
                .peek(item -> this.getListeners().forEach(listener -> listener.onData(item))).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<T>> readAll(Workbook workbook, int headRdRows) {
        int numberOfSheets = workbook.getNumberOfSheets();
        Map<String, List<T>> result = new LinkedHashMap<>();
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
     * @param dataMeta 表元数据
     * @return 转换器提供者
     */
    protected ConverterProvider findConverterProvider(DataMeta dataMeta) {
        if (dataMeta instanceof ConverterProviderWrapper) {
            ConverterProviderWrapper wrapper = (ConverterProviderWrapper) dataMeta;
            return wrapper.getConverterProvider();
        }
        return null;
    }

    /**
     * 获取转换器
     *
     * @param dataMeta 表元数据
     * @return 转换器
     */
    protected Converter<?, ?> findConverter(DataMeta dataMeta) {
        ConverterProvider converterProvider = this.findConverterProvider(dataMeta);
        if (converterProvider != null) {
            return this.converterFactory.findConverter(converterProvider.getConverter());
        }
        return null;
    }

}
