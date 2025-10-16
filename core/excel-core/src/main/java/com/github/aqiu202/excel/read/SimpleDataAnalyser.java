package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.Converter;
import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class SimpleDataAnalyser implements DataAnalyser {

    private final CellReader cellReader = new SimpleCellReader();
    private final IndexedMetaResolver indexedMetaResolver = new SimpleIndexedMetaResolver();
    private final MetaAnalyzer<?> metaAnalyzer;

    public SimpleDataAnalyser() {
        this(null);
    }

    public SimpleDataAnalyser(MetaAnalyzer<?> metaAnalyzer) {
        this.metaAnalyzer = metaAnalyzer;
    }

    public CellReader getCellReader() {
        return cellReader;
    }

    public IndexedMetaResolver getIndexedMetaResolver() {
        return indexedMetaResolver;
    }

    public MetaAnalyzer<?> getMetaAnalyzer() {
        return metaAnalyzer;
    }

    @Override
    public CellVal<?> readConvertedCellValue(Cell cell, SheetReadConfiguration configuration, Converter converter) {
        CellVal<?> cellVal = this.getCellReader().readCell(cell, configuration);
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
    public List<IndexedTableMeta> analyse(Sheet sheet, Class<?> type, int startColIndex, int columns, int headRows) {
        MetaAnalyzer<?> metaAnalyzer = this.getMetaAnalyzer();
        if (metaAnalyzer == null) {
            return Collections.emptyList();
        }
        List<? extends TableMeta> metas = this.analyseMetas(type);
        HeadMeta[] headMetas = this.readHeadMetas(sheet, startColIndex, columns, headRows);
        return this.resolveIndexedTableMeta(type, metas, headMetas);
    }

    protected List<? extends TableMeta> analyseMetas(Class<?> type) {
        MetaAnalyzer<?> metaAnalyzer = this.getMetaAnalyzer();
        if (metaAnalyzer == null) {
            return Collections.emptyList();
        }
        return this.getMetaAnalyzer().analyseMetas(type);
    }

    protected HeadMeta[] readHeadMetas(Sheet sheet, int startColIndex, int columns, int headRows) {
        return this.getCellReader().readHeads(sheet, startColIndex, columns, headRows);
    }

    protected List<IndexedTableMeta> resolveIndexedTableMeta(Class<?> type, List<? extends TableMeta> metas, HeadMeta[] headMetas) {
        return this.getIndexedMetaResolver().resolve(type, metas, headMetas);
    }

}
