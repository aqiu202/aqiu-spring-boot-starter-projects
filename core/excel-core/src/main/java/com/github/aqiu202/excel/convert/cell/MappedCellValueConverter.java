package com.github.aqiu202.excel.convert.cell;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.MappedCellValue;

public interface MappedCellValueConverter {

    void convert(MappedCellValue mappedCellValue, FormatterProvider formatterProvider);

}
