package com.github.aqiu202.id;

import java.io.Serializable;
import java.security.SecureRandom;
import org.springframework.lang.NonNull;

/**
 * <pre>IdGenerator</pre>
 *
 * @author aqiu 2020/12/2 10:12
 **/
public interface IdGenerator<T extends Serializable> {

    @NonNull
    T generateId();

    default long getWordId() {
        return new SecureRandom().nextInt(31);
    }

    default long getDataCenterId() {
        return new SecureRandom().nextInt(31);
    }
}
