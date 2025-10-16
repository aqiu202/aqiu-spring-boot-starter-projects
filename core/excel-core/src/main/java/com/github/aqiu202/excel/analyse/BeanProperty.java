package com.github.aqiu202.excel.analyse;

public class BeanProperty {

    private final String propertyName;
    private final String[] propertyTitles;

    public BeanProperty(String propertyName, String... propertyTitles) {
        this.propertyName = propertyName;
        this.propertyTitles = propertyTitles;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String[] getPropertyTitles() {
        return propertyTitles;
    }
}
