package com.github.aqiu202.excel.read.convert;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.MappedCellValue;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

public class NoneRowMappedCellValuesConverter implements RowMappedCellValuesConverter {

    public static final NoneRowMappedCellValuesConverter INSTANCE = new NoneRowMappedCellValuesConverter();

    public NoneRowMappedCellValuesConverter() {
    }

    @Override
    public MappedCellValueConverter getMappedCellValueConverter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void convert(RowMappedCellValues rowMappedCellValues, FormatterProvider formatterProvider) {

    }
}
