package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.MappingMeta;

/**
 * 表头与JavaBean属性/Map的映射关系
 */
public interface MappedProperty {

    MappingMeta getMappingMeta();

    BeanProperty getBeanProperty();

    default boolean hasProperty() {
        return this.getBeanProperty() != null;
    }
    default boolean nonProperty() {
        return !this.hasProperty();
    }

}
