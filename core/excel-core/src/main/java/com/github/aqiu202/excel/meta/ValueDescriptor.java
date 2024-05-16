package com.github.aqiu202.excel.meta;

public interface ValueDescriptor {

    Class<?> getValueType();

    Object getValue(Object instance);

    void setValue(Object instance, Object value);
}
