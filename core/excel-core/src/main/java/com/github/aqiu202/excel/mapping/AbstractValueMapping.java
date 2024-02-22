package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.prop.MappedProperty;
import com.github.aqiu202.excel.prop.MappedPropertyFactory;
import com.github.aqiu202.excel.read.cell.CellValue;

import java.util.List;

public abstract class AbstractValueMapping<T extends MappingMeta> implements ValueMapping<T> {

    @Override
    public MappedProperty getMappedProperty(CellValue<?> cv, Class<?> type, MappedPropertyFactory factory, PropertyAccessor propertyAccessor) {
        List<T> meta = this.analysisMetas(type);
        return factory.create(this.select(cv, meta), type, propertyAccessor);
    }

    @Override
    public T select(CellValue<?> cv, List<T> fields) {
        return fields.stream().filter(meta -> meta.match(cv)).findFirst().orElse(null);
    }

}
