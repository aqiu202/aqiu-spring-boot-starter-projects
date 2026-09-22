package com.github.aqiu202.id;

import com.github.aqiu202.id.type.IdType;
import jakarta.annotation.Nonnull;

/**
 * <pre>IdGeneratorFactory</pre>
 *
 * @author aqiu 2020/12/2 10:12
 **/
@FunctionalInterface
public interface IdGeneratorFactory {

    @Nonnull
    IdGenerator<?> getIdGenerator();

    default IdType getIdType() {
        return IdType.AUTO;
    }

}
