package com.github.aqiu202.starters.jpa.bean;

import java.lang.reflect.Field;
import java.util.Objects;
import org.springframework.util.StringUtils;

public final class JavaBeanMethod {

    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";

    public JavaBeanMethod() {

    }

    private JavaBeanMethod(Field field) {
        this.type = field.getType();
        this.fieldName = field.getName();
        String _fieldName = StringUtils.capitalize(this.fieldName);
        this.writeMethodName = SET_PREFIX + _fieldName;
        if (boolean.class.equals(this.type)) {
            this.readMethodName = IS_PREFIX + _fieldName;
        } else {
            this.readMethodName = GET_PREFIX + _fieldName;
        }
    }

    public static JavaBeanMethod of(Field field) {
        return new JavaBeanMethod(field);
    }

    private String readMethodName;

    private String writeMethodName;

    private Class<?> type;

    private String fieldName;

    private int writeIndex = -1;

    private int readIndex = -1;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getReadMethodName() {
        return readMethodName;
    }

    public void setReadMethodName(String readMethodName) {
        this.readMethodName = readMethodName;
    }

    public String getWriteMethodName() {
        return writeMethodName;
    }

    public void setWriteMethodName(String writeMethodName) {
        this.writeMethodName = writeMethodName;
    }

    public int getWriteIndex() {
        return writeIndex;
    }

    public void setWriteIndex(int writeIndex) {
        this.writeIndex = writeIndex;
    }

    public int getReadIndex() {
        return readIndex;
    }

    public void setReadIndex(int readIndex) {
        this.readIndex = readIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaBeanMethod method = (JavaBeanMethod) o;
        return Objects.equals(type, method.type) &&
                Objects.equals(fieldName, method.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, fieldName);
    }
}
