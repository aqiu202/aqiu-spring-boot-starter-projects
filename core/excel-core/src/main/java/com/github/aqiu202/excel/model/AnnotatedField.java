package com.github.aqiu202.excel.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

public class AnnotatedField<T extends Annotation> {

    protected final Field field;
    protected final T annotation;

    public AnnotatedField(Field field, T annotation) {
        this.field = field;
        this.annotation = annotation;
    }
    public AnnotatedField(Field field) {
        this(field, null);
    }

    public T getAnnotation() {
        return annotation;
    }

    public Field getField() {
        return field;
    }

    public boolean hasAnnotation() {
        return Objects.nonNull(this.annotation);
    }
}
