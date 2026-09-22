package com.github.aqiu202.id;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

/**
 * <pre>IdGenerator</pre>
 *
 * @author aqiu 2020/12/2 10:12
 **/
@FunctionalInterface
public interface IdGenerator<T extends Serializable> {

    @Nonnull
    T nextId();

}
