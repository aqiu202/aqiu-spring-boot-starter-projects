package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.PropertyAccessor;

public interface MappedPropertyFactory {

    /**
     * 创建映射关系
     * @param meta 元数据
     * @param type 目标类型
     * @param propertyAccessor 访问方式
     */
    MappedProperty create(MappingMeta meta, Class<?> type, PropertyAccessor propertyAccessor);

}
