package com.github.aqiu202.excel.convert.cell;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

public interface RowMappedCellValuesConverter {
    void setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter);

    MappedCellValueConverter getMappedCellValueConverter();

    void convert(RowMappedCellValues rowMappedCellValues, FormatterProvider formatterProvider);
}
