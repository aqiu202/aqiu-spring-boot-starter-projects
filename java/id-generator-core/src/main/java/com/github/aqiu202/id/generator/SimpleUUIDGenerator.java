package com.github.aqiu202.id.generator;

import com.github.aqiu202.id.IdGenerator;
import java.util.UUID;
import org.springframework.lang.NonNull;

/**
 * <pre>SimpleUUIDGenerator</pre>
 *
 * @author aqiu 2020/12/2 15:25
 **/
public class SimpleUUIDGenerator implements IdGenerator<String> {

    @NonNull
    @Override
    public String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
