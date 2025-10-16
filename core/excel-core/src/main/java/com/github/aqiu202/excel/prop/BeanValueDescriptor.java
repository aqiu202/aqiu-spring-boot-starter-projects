package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.meta.ValueDescriptor;

/**
 * JavaBean属性（用于读取/设置JavaBean属性）
 */
public interface BeanValueDescriptor extends ValueDescriptor {

    Class<?> getBeanType();

    String getPropertyName();

    boolean isValid();
}
