package com.github.aqiu202.excel.prop;

import com.github.aqiu202.util.StringUtils;

public abstract class AbstractPathElements<T> implements PathElements<T> {

    private final T[] content;

    public AbstractPathElements(Class<?> type, String propertyName) {
        T[] elements;
        if (StringUtils.isNotBlank(propertyName)) {
            String[] fs = propertyName.split("\\.");
            elements = this.createEmptyElementArray(fs.length);
            T item;
            Class<?> c = type;
            for (int i = 0; i < fs.length; i++) {
                String f = fs[i];
                item = this.createElement(c, f);
                elements[i] = item;
                c = this.getType(item);
            }
        } else {
            elements = this.createEmptyElementArray(0);
        }
        this.content = elements;
    }

    @Override
    public T[] getContent() {
        return content;
    }
}
