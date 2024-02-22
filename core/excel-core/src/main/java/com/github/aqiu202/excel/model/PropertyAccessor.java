package com.github.aqiu202.excel.model;

/**
 * JavaBean字段读写方式
 */
public enum PropertyAccessor {

    /**
     * 基于{@link com.github.aqiu202.excel.prop.FieldBeanProperty}读写数据
     */
    FIELD,
    /**
     * 基于{@link com.github.aqiu202.excel.prop.MethodBeanProperty}读写数据
     */
    METHOD,
    /**
     * 暂未实现
     */
//    AUTO
}
