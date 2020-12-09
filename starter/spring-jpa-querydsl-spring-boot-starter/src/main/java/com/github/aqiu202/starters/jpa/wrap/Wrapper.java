package com.github.aqiu202.starters.jpa.wrap;

public interface Wrapper<T> {

    void set(T t);

    T get();
}
