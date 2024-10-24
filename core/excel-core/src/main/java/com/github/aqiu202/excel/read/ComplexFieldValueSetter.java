package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.meta.ValueDescriptor;
import com.github.aqiu202.excel.read.cell.CellVal;
import com.github.aqiu202.excel.read.cell.ConvertedCellVal;
import com.github.aqiu202.excel.read.cell.MappedCellValue;

import javax.annotation.Nullable;

public class ComplexFieldValueSetter implements FieldValueSetter {

    @Override
    public void set(Object instance, @Nullable MappedCellValue mappedCellValue) {
        if (mappedCellValue == null) {
            return;
        }
        DataMeta dataMeta = mappedCellValue.getTableMeta();
        ValueDescriptor vd = dataMeta.getValueDescriptor();
        CellVal<?> cellVal = mappedCellValue.getCellValue();
        if (cellVal instanceof ConvertedCellVal) {
            cellVal = ((ConvertedCellVal) cellVal).getValue();
        }
        vd.setValue(instance, this.extractCellValueWithType(cellVal, vd.getValueType()));
    }
}
