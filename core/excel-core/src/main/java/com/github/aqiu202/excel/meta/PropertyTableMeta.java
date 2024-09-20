package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.prop.MapBeanProperty;

public class PropertyTableMeta implements TableMeta {

    private final String fieldName;
    private final String[] fieldTitles;
    private boolean image;

    public PropertyTableMeta(String fieldName) {
        this(fieldName, new String[0]);
    }

    public PropertyTableMeta(String fieldName, String... fieldTitles) {
        this.fieldName = fieldName;
        this.fieldTitles = fieldTitles;
    }

    @Override
    public String getKey() {
        return this.fieldName;
    }

    @Override
    public ValueDescriptor getValueDescriptor() {
        return new MapBeanProperty(this.fieldName);
    }


    @Override
    public HeadDescriptor getHeadDescriptor() {
        return new SimpleHeadDescriptor(this.fieldTitles);
    }

    @Override
    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
