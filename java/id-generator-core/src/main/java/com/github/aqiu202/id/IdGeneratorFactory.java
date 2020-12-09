package com.github.aqiu202.id;

import com.github.aqiu202.id.type.IdType;
import java.io.Serializable;
import org.omg.CORBA.IDLType;
import org.springframework.lang.NonNull;

/**
 * <pre>IdGeneratorFactory</pre>
 *
 * @author aqiu 2020/12/2 10:12
 **/
@FunctionalInterface
public interface IdGeneratorFactory<T extends Serializable> {

    @NonNull
    IdGenerator<T> getIdGenerator();

    default IdType getIdType() {
        return IdType.AUTO;
    }

}
