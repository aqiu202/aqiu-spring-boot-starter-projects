package com.github.aqiu202.excel.read.convert;

import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.read.cell.MappedCellValue;
import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

public class SimpleRowMappedCellValuesConverter implements RowMappedCellValuesConverter {

    private MappedCellValueConverter mappedCellValueConverter;

    public SimpleRowMappedCellValuesConverter(MappedCellValueConverter mappedCellValueConverter) {
        this.mappedCellValueConverter = mappedCellValueConverter;
    }

    public SimpleRowMappedCellValuesConverter() {
        this(new SimpleMappedCellValueConverter());
    }

    @Override
    public MappedCellValueConverter getMappedCellValueConverter() {
        return mappedCellValueConverter;
    }

    @Override
    public void setMappedCellValueConverter(MappedCellValueConverter mappedCellValueConverter) {
        this.mappedCellValueConverter = mappedCellValueConverter;
    }

    @Override
    public void convert(RowMappedCellValues rowMappedCellValues, FormatterProvider formatterProvider) {
        MappedCellValue[] mappedCellValues = rowMappedCellValues.getMappedCellValues();
        for (MappedCellValue mappedCellValue : mappedCellValues) {
            if (mappedCellValue != null) {
                this.mappedCellValueConverter.convert(mappedCellValue, formatterProvider);
            }
        }
    }
}
