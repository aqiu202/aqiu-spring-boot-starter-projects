package com.github.aqiu202.excel.read.convert;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.MappedCellValue;

import javax.annotation.Nonnull;

public interface MappedCellValueConverter {

    void convert(@Nonnull MappedCellValue mappedCellValue, FormatterProvider formatterProvider);

}
