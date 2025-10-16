package com.github.aqiu202.excel.model;

import com.github.aqiu202.excel.prop.FieldBeanValueDescriptor;
import com.github.aqiu202.excel.prop.MethodBeanValueDescriptor;
import com.github.aqiu202.util.StringUtils;

/**
 * JavaBean字段读写方式
 */
public enum PropertyAccessor {

    /**
     * 基于{@link FieldBeanValueDescriptor}读写数据
     */
    FIELD,
    /**
     * 基于{@link MethodBeanValueDescriptor}读写数据
     */
    METHOD;

    public static PropertyAccessor parse(String name) {
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.equalsIgnoreCase("METHOD", name)) {
                return METHOD;
            }
        }
        return FIELD;
    }

}
