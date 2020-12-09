package com.github.aqiu202.starters.jpa.type;


import com.github.aqiu202.starters.jpa.wrap.ObjectWrapper;

public interface TypeConverter {

    void convert(ObjectWrapper wrapper, Class<?> type);

}
