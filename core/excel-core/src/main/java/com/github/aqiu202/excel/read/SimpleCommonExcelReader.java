package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.cell.MappedCellValueConverter;
import com.github.aqiu202.excel.convert.cell.RowMappedCellValuesConverter;
import com.github.aqiu202.excel.convert.cell.SimpleRowMappedCellValuesConverter;
import com.github.aqiu202.excel.mapping.FieldValueMapping;
import com.github.aqiu202.excel.mapping.ValueMapping;
import com.github.aqiu202.excel.model.ReadDataFilter;
import com.github.aqiu202.excel.model.ReadDataListener;
import com.github.aqiu202.excel.model.SheetDataConfiguration;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.prop.MappedProperty;
import com.github.aqiu202.excel.prop.MappedPropertyFactory;
import com.github.aqiu202.excel.prop.SimpleMappedPropertyFactory;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleCommonExcelReader<T> implements CommonExcelReader<T> {
    private final RowMappedCellValuesConverter rowMappedCellValuesConverter = new SimpleRowMappedCellValuesConverter();
    private final MappedPropertyFactory mappedPropertyFactory = new SimpleMappedPropertyFactory();
    private final List<ReadDataListener<T>> listeners = new ArrayList<>();
    private final CellReader cellReader = new SimpleCellReader();
    private final Class<T> type;
    private TypeTranslator typeTranslator = new SimpleTypeTranslator(new ComplexFieldValueSetter());
    private ValueMapping<?> valueMapping;
    private ReadDataFilter<RowMappedCellValues> rawDataFilter;
    private ReadDataFilter<T> filter;

    public SimpleCommonExcelReader(Class<T> type) {
        this(type, new FieldValueMapping());
    }

    public SimpleCommonExcelReader(Class<T> type, ValueMapping<?> valueMapping) {
        if (ClassUtils.isCustomClass(type) || ClassUtils.isAssignableFrom(Map.class, type)) {
            this.type = type;
        } else {
            throw new RuntimeException("不支持的类型");
        }
        this.valueMapping = valueMapping;
    }

    public Class<T> getType() {
        return this.type;
    }

    private SheetReadConfiguration configuration = new SheetReadConfiguration();
    private SheetDataConfiguration globalConfiguration = new SheetDataConfiguration();

    public SheetReadConfiguration getConfiguration() {
        return configuration;
    }

    public SimpleCommonExcelReader<T> globalConfiguration(SheetDataConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
        return this;
    }

    public SheetDataConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public MappedCellValueConverter getMappedCellValueConverter() {
        return this.rowMappedCellValuesConverter.getMappedCellValueConverter();
    }

    @Override
    public SimpleCommonExcelReader<T> setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter) {
        this.rowMappedCellValuesConverter.setMappedCellValueConverter(mappedCellValueConverter);
        return this;
    }

    public CellReader getCellReader() {
        return cellReader;
    }

    public MappedPropertyFactory getMappedPropertyFactory() {
        return mappedPropertyFactory;
    }

    public TypeTranslator getTypeTranslator() {
        return typeTranslator;
    }

    @Override
    public SimpleCommonExcelReader<T> setTypeTranslator(TypeTranslator typeTranslator) {
        this.typeTranslator = typeTranslator;
        return this;
    }

    public ValueMapping<?> getValueMapping() {
        return valueMapping;
    }

    @Override
    public SimpleCommonExcelReader<T> valueMapping(ValueMapping<?> valueMapping) {
        this.valueMapping = valueMapping;
        return this;
    }

    public SimpleCommonExcelReader<T> configuration(SheetReadConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public CommonExcelReader<T> addListener(ReadDataListener<T> listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public CommonExcelReader<T> filter(ReadDataFilter<T> filter) {
        this.filter = filter;
        return this;
    }

    public ReadDataFilter<RowMappedCellValues> getRawDataFilter() {
        return rawDataFilter;
    }

    @Override
    public SimpleCommonExcelReader<T> rawDataFilter(ReadDataFilter<RowMappedCellValues> rawDataFilter) {
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
    public List<T> read(Workbook workbook, int sheetIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new RuntimeException("Sheet页不存在");
        }
        SheetReadConfiguration configuration = this.getConfiguration();
        int minRowNum = Math.max(configuration.getStartRowNumber(), 0);
        int maxRowNum = Math.min(configuration.getEndRowNumber(), sheet.getLastRowNum());
        // headRow 取 title
        Row headRow = sheet.getRow(minRowNum);
        int minColumnNum = Math.max(configuration.getStartColumnNumber(), 0);
        int maxColumnNum = Math.min(configuration.getEndColumnNumber(), headRow.getLastCellNum());
        MappedProperty[] mappedProperties = new MappedProperty[maxColumnNum - minColumnNum];
        CellReader cellReader = this.getCellReader();
        for (int i = minColumnNum; i < maxColumnNum; i++) {
            Cell headCell = headRow.getCell(i);
            CellValue<?> cellValue = cellReader.readCell(headCell);
            mappedProperties[i - minColumnNum] = this.getValueMapping().getMappedProperty(cellValue, this.getType(),
                    this.getMappedPropertyFactory(), configuration.getPropertyAccessor());
        }
        List<RowMappedCellValues> mappedCellValues = new ArrayList<>();
        for (int i = minRowNum + 1; i <= maxRowNum; i++) {
            Row row = sheet.getRow(i);
            RowMappedCellValues rowCellValues = new SimpleRowMappedCellValues(i, mappedProperties.length);
            for (int j = minColumnNum; j < maxColumnNum; j++) {
                Cell cell = row.getCell(j);
                int currentIndex = j - minColumnNum;
                rowCellValues.setMappedCellValue(currentIndex, new SimpleMappedCellValue(mappedProperties[currentIndex],
                        cellReader.readCell(cell)));
            }
            mappedCellValues.add(rowCellValues);
        }
        return mappedCellValues.stream()
                .peek(values -> this.rowMappedCellValuesConverter.convert(values, this.getConfiguration()))
                .filter(item -> this.getRawDataFilter() == null || this.getRawDataFilter().test(item))
                .map(values -> this.getTypeTranslator().translate(values, this.getType()))
                .filter(item -> this.getFilter() == null || this.getFilter().test(item))
                .peek(item -> this.getListeners().forEach(listener -> listener.onData(item))).collect(Collectors.toList());
    }

}
