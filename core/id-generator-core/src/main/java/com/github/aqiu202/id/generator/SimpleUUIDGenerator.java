package com.github.aqiu202.id.generator;

import com.github.aqiu202.id.IdGenerator;
import java.util.UUID;

import jakarta.annotation.Nonnull;

/**
 * <pre>SimpleUUIDGenerator</pre>
 *
 * @author aqiu 2020/12/2 15:25
 **/
public class SimpleUUIDGenerator implements IdGenerator<String> {

    @Nonnull
    @Override
    public String nextId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
