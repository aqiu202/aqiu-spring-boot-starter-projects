package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.Converter;
import com.github.aqiu202.excel.meta.IndexedMeta;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.model.ReadConfiguration;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;
import java.util.List;

public class SimpleDataAnalyser implements DataAnalyser {

    private final CellReader cellReader = new SimpleCellReader();
    private final IndexedMetaResolver indexedMetaResolver = new SimpleIndexedMetaResolver();
    private final MetaAnalyzer<?> metaAnalyzer;

    public SimpleDataAnalyser(MetaAnalyzer<?> metaAnalyzer) {
        this.metaAnalyzer = metaAnalyzer;
    }

    @Override
    public CellVal<?> readConvertedCellValue(Cell cell, ReadConfiguration configuration, Converter converter) {
        CellVal<?> cellVal = this.cellReader.readCell(cell, configuration);
        if (converter != null) {
            Object value = cellVal.getValue();
            Object convertedValue = converter.from(value);
            if (convertedValue == null) {
                return new BlankCellVal(cell);
            }
            if (ClassUtils.isDate(convertedValue)) {
                return DateCellVal.of(cell, convertedValue);
            }
            if (ClassUtils.isNumber(convertedValue)) {
                return new NumberCellVal(cell, new BigDecimal(String.valueOf(convertedValue)));
            }
            if (convertedValue instanceof String) {
                return new StringCellVal(cell, (String) convertedValue);
            }
            if (convertedValue instanceof Boolean) {
                return new BooleanCellVal(cell, (Boolean) convertedValue);
            }
        }
        return cellVal;
    }

    @Override
    public List<IndexedMeta> analyse(Sheet sheet, Class<?> type, int startColIndex, int columns, int headRows) {
        List<? extends DataMeta> metas = this.metaAnalyzer.analyseMetas(type);
        HeadMeta[] headMetas = cellReader.readHeads(sheet, startColIndex, columns, headRows);
        return this.indexedMetaResolver.resolve(type, metas, headMetas);
    }

}
