package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.meta.MetaAnalyzer;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.prop.MappedProperty;
import com.github.aqiu202.excel.prop.MappedPropertyFactory;
import com.github.aqiu202.excel.read.cell.CellValue;

import java.util.List;

/**
 * 读取Excel时，取值映射器
 */
public interface ValueMapping<T extends MappingMeta> extends MetaAnalyzer<T> {

    /**
     * 根据表头值选取相应的元数据
     *
     * @param cv    表头值
     * @param metas 映射元数据集合
     * @return 匹配表头值的映射元数据
     */
    T select(CellValue<?> cv, List<T> metas);

    MappedProperty getMappedProperty(CellValue<?> cv, Class<?> type, MappedPropertyFactory factory, PropertyAccessor propertyAccessor);

}
