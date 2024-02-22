package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.prop.BeanProperty;
import com.github.aqiu202.excel.prop.MappedProperty;
import com.github.aqiu202.excel.read.cell.CellValue;
import com.github.aqiu202.excel.read.cell.ConvertedCellValue;
import com.github.aqiu202.excel.read.cell.MappedCellValue;

public class ComplexFieldValueSetter implements FieldValueSetter {

    @Override
    public void set(Object instance, MappedCellValue mappedCellValue) {
        MappedProperty mappedProperty = mappedCellValue.getMappedProperty();
        MappingMeta mappingMeta = mappedProperty.getMappingMeta();
        if (mappingMeta == null || mappedProperty.nonProperty()) {
            return;
        }
        BeanProperty beanProperty = mappedProperty.getBeanProperty();
        if (!beanProperty.isValid()) {
            return;
        }
        CellValue<?> cellValue = mappedCellValue.getCellValue();
        if (cellValue instanceof ConvertedCellValue) {
            cellValue = ((ConvertedCellValue) cellValue).getValue();
        }
        beanProperty.setValue(instance, this.extractCellValueWithType(cellValue, beanProperty.getPropertyType()));
    }
}
