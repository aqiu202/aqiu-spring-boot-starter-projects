package com.github.aqiu202.excel.write.hand;

import java.util.List;

public interface Handlers<T> {

    void addHandler(T handler);

    List<T> getHandlers();
}
