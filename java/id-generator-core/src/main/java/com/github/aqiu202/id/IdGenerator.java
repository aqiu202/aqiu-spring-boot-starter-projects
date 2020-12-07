package com.github.aqiu202.id;

import java.io.Serializable;
import org.springframework.lang.NonNull;

/**
 * <pre>IdGenerator</pre>
 *
 * @author aqiu 2020/12/2 10:12
 **/
@FunctionalInterface
public interface IdGenerator<T extends Serializable> {

    @NonNull
    T generateId();

}
