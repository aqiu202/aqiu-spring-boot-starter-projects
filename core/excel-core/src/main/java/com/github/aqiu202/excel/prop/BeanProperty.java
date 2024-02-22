package com.github.aqiu202.excel.prop;

/**
 * JavaBean属性（用于读取/设置JavaBean属性）
 */
public interface BeanProperty {

    Class<?> getBeanType();

    Class<?> getPropertyType();

    String getPropertyName();

    void setValue(Object instance, Object value);

    Object getValue(Object instance);

    boolean isValid();
}
