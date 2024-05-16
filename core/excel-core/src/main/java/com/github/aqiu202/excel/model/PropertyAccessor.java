package com.github.aqiu202.excel.model;

import com.github.aqiu202.util.StringUtils;

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
    METHOD;

    public static PropertyAccessor parse(String name) {
        if (StringUtils.isNotBlank(name)) {
            String un = name.toUpperCase();
            if (un.equals("METHOD")) {
                return METHOD;
            }
        }
        return FIELD;
    }

}
