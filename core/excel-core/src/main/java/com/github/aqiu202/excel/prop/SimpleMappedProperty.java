package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.MappingMeta;

public class SimpleMappedProperty implements MappedProperty {

    private final MappingMeta mappingMeta;
    private final BeanProperty beanProperty;

    public SimpleMappedProperty(MappingMeta mappingMeta) {
        this(mappingMeta, null);
    }

    public SimpleMappedProperty(MappingMeta mappingMeta, BeanProperty beanProperty) {
        this.mappingMeta = mappingMeta;
        this.beanProperty = beanProperty;
    }

    @Override
    public MappingMeta getMappingMeta() {
        return this.mappingMeta;
    }

    @Override
    public BeanProperty getBeanProperty() {
        return this.beanProperty;
    }

    @Override
    public String toString() {
        if (this.mappingMeta == null) {
            return null;
        }
        return this.mappingMeta.getFieldName() + ":" + this.mappingMeta.getFieldTitle();
    }
}
