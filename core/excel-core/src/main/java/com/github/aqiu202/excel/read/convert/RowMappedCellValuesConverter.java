package com.github.aqiu202.excel.read.convert;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

/**
 * 将读取的每行数据进行数据转换/格式化
 */
public interface RowMappedCellValuesConverter {
    void setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter);

    MappedCellValueConverter getMappedCellValueConverter();

    void convert(RowMappedCellValues rowMappedCellValues, FormatterProvider formatterProvider);
}
