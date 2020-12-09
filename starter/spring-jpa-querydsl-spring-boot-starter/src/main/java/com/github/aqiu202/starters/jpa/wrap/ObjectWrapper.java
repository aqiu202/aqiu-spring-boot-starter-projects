package com.github.aqiu202.starters.jpa.wrap;

public class ObjectWrapper implements Wrapper<Object> {

    public ObjectWrapper(Object value) {
        this.value = value;
    }

    private Object value;

    @Override
    public void set(Object o) {
        this.value = o;
    }

    @Override
    public Object get() {
        return this.value;
    }
}
