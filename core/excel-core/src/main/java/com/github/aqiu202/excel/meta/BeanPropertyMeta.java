package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.analyse.BeanProperty;
import com.github.aqiu202.excel.prop.FieldBeanValueDescriptor;

public class BeanPropertyMeta implements TableMeta {

    private final Class<?> beanType;
    private final String propertyName;
    private final String[] propertyTitles;
    private boolean image;

    public BeanPropertyMeta(Class<?> beanType, String propertyName) {
        this(beanType, propertyName, new String[0]);
    }

    public BeanPropertyMeta(Class<?> beanType, BeanProperty beanProperty) {
        this(beanType, beanProperty.getPropertyName(), beanProperty.getPropertyTitles());
    }

    public BeanPropertyMeta(Class<?> beanType, String propertyName, String... propertyTitles) {
        this.beanType = beanType;
        this.propertyName = propertyName;
        this.propertyTitles = propertyTitles;
    }

    @Override
    public String getKey() {
        return this.propertyName;
    }

    @Override
    public ValueDescriptor getValueDescriptor() {
        return new FieldBeanValueDescriptor(this.beanType, this.propertyName);
    }


    @Override
    public HeadDescriptor getHeadDescriptor() {
        return new SimpleHeadDescriptor(this.propertyTitles);
    }

    @Override
    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
