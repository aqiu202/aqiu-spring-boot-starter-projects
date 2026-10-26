package com.github.aqiu202.http.util;

import java.lang.reflect.Type;

public class TypeSpec<T> {

    private final Type type;

    public TypeSpec(Class<T> type) {
        this.type = type;
    }

    public TypeSpec(ParameterizedTypeRef<T> type) {
        this.type = type.getType();
    }

    /**
     * 直接使用反射类型，例如 Jackson 的 {@code JavaType}（其实现了 {@link Type}）。
     */
    public TypeSpec(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
