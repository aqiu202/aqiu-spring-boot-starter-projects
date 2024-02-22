package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.model.PropertyAccessor;

import java.util.Map;

public class SimpleMappedPropertyFactory implements MappedPropertyFactory {

    @Override
    public MappedProperty create(MappingMeta meta, Class<?> type, PropertyAccessor propertyAccessor) {
        if (meta == null) {
            return new SimpleMappedProperty(null);
        }
        BeanProperty beanProperty;
        if (Map.class.isAssignableFrom(type)) {
            beanProperty = new MapBeanProperty(meta.getFieldName());
        } else {
            beanProperty = new ProxyBeanProperty(type, meta.getFieldName(), propertyAccessor);
        }
        return new SimpleMappedProperty(meta, beanProperty);
    }


}
