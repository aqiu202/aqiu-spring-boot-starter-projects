package com.github.aqiu202.starters.jpa.id;


@FunctionalInterface
public interface CustomKeyGenerator {

    Object generate(Object owner, Object currentValue);
}
