package com.github.aqiu202.excel.write.hand;

import java.util.ArrayList;
import java.util.List;

public class SimpleHandlers<T> implements Handlers<T> {

    private final List<T> handlers = new ArrayList<>();

    @Override
    public void addHandler(T handler) {
        this.handlers.add(handler);
    }

    @Override
    public List<T> getHandlers() {
        return this.handlers;
    }
}
