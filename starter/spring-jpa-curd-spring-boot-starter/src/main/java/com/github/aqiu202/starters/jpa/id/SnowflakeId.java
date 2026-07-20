package com.github.aqiu202.starters.jpa.id;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@IdGeneratorType(SnowflakeEntityIdGenerator.class)
public @interface SnowflakeId {
}
