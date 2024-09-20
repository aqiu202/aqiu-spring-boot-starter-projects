package com.github.aqiu202.excel.prop;

import java.util.Arrays;

public interface PathElements<T> {

    T[] getContent();

    default boolean isEmpty() {
        T[] content = this.getContent();
        return content == null || content.length == 0;
    }

    default int length() {
        if (this.isEmpty()) {
            return 0;
        }
        return this.getContent().length;
    }

    default T get(int index) {
        if (index < 0 || index > this.length() - 1) {
            return null;
        }
        return this.getContent()[index];
    }

    default void set(int index, T item) {
        if (index < 0 || index > this.length() - 1) {
            return;
        }
        this.getContent()[index] = item;
    }

    /**
     * 获取最后一个元素
     */
    default T getLastElement() {
        T[] content = this.getContent();
        if (content.length == 0) {
            return null;
        }
        return content[content.length - 1];
    }

    /**
     * 获取除最后一个元素外的所有元素
     */
    default T[] getPreElements() {
        T[] content = this.getContent();
        if (content.length <= 1) {
            return null;
        }
        return Arrays.copyOfRange(content, 0, content.length - 1);
    }

    /**
     * 获取最后一个元素的类型
     */
    default Class<?> getLastElementType() {
        T element = this.getLastElement();
        if (element == null) {
            return null;
        }
        return this.getType(element);
    }

    T createElement(Class<?> beanType, String propertyName);

    T[] createEmptyElementArray(int length);

    Class<?> getType(T element);


}
