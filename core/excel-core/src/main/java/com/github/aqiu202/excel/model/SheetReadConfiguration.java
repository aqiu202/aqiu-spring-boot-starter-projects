package com.github.aqiu202.excel.model;

public class SheetReadConfiguration extends SheetDataConfiguration {
    private int startRowNumber = 0;
    private int endRowNumber = Integer.MAX_VALUE;

    private int startColumnNumber;
    private int endColumnNumber = Integer.MAX_VALUE;

    private PropertyAccessor propertyAccessor = PropertyAccessor.FIELD;

    public int getStartRowNumber() {
        return startRowNumber;
    }

    public void setStartRowNumber(int startRowNumber) {
        this.startRowNumber = startRowNumber;
    }

    public int getEndRowNumber() {
        return endRowNumber;
    }

    public void setEndRowNumber(int endRowNumber) {
        this.endRowNumber = endRowNumber;
    }

    public int getStartColumnNumber() {
        return startColumnNumber;
    }

    public void setStartColumnNumber(int startColumnNumber) {
        this.startColumnNumber = startColumnNumber;
    }

    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    public void setEndColumnNumber(int endColumnNumber) {
        this.endColumnNumber = endColumnNumber;
    }

    public PropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }

    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }
}
