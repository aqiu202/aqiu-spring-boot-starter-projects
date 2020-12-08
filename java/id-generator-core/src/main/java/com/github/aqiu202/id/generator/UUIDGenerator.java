package com.github.aqiu202.id.generator;

import com.github.aqiu202.id.IdGenerator;
import java.util.UUID;
import org.springframework.lang.NonNull;

/**
 * <pre>UUIDGenerator</pre>
 *
 * @author aqiu 2020/12/2 15:25
 **/
public class UUIDGenerator implements IdGenerator<String> {

    @NonNull
    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
