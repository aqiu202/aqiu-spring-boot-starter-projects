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

    public Type getType() {
        return type;
    }

}
