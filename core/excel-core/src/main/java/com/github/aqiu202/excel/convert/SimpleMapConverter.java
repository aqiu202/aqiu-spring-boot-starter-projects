package com.github.aqiu202.excel.convert;

import java.util.Map;

public class SimpleMapConverter<S, T> implements MapConverter<S, T> {

    private final Map<S, T> map;

    public SimpleMapConverter(Map<S, T> map) {
        this.map = map;
    }

    @Override
    public Map<S, T> getMap() {
        return this.map;
    }

    @Override
    public T convert(S source) {
        return this.getMap().get(source);
    }

    @Override
    public S from(T target) {
        for (Map.Entry<S, T> entry : this.map.entrySet()) {
            if (entry.getValue().equals(target)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
