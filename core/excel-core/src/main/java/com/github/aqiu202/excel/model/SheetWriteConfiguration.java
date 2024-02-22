package com.github.aqiu202.excel.model;

import com.github.aqiu202.excel.style.FontProperty;
import com.github.aqiu202.excel.style.StyleProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


public class SheetWriteConfiguration extends SheetDataConfiguration {
    public SheetWriteConfiguration() {
        FontProperty font = this.headStyle.getFont();
        font.setBold(true);
        font.setFontSize((short) 12);
    }

    public static final int DEFAULT_ROW_ACCESS_WINDOW_SIZE = 2000;

    public static final String DEFAULT_WRITE_DATE_FORMAT = "yyyy/M/d h:mm";
    private int rowAccessWindowSize = DEFAULT_ROW_ACCESS_WINDOW_SIZE;

//    private PropertyAccessor propertyAccessor = PropertyAccessor.FIELD;

    private WorkbookType workBookType = WorkbookType.SXSSF;

    private double autoWidthRatio = 1;

    @NestedConfigurationProperty
    private StyleProperty headStyle = new StyleProperty();
    @NestedConfigurationProperty
    private StyleProperty contentStyle = new StyleProperty();

    private boolean autoSizeColumn = true;

    /**
     * 日期的默认格式化（excel中日期格式的数据的显示格式）
     */
    private String defaultDateFormat = DEFAULT_WRITE_DATE_FORMAT;

    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public boolean isAutoSizeColumn() {
        return autoSizeColumn;
    }

    public void setAutoSizeColumn(boolean autoSizeColumn) {
        this.autoSizeColumn = autoSizeColumn;
    }

    public int getRowAccessWindowSize() {
        return rowAccessWindowSize;
    }

    public void setRowAccessWindowSize(int rowAccessWindowSize) {
        this.rowAccessWindowSize = rowAccessWindowSize;
    }

    public StyleProperty getHeadStyle() {
        return headStyle;
    }

    public void setHeadStyle(StyleProperty headStyle) {
        this.headStyle = headStyle;
    }

    public StyleProperty getContentStyle() {
        return contentStyle;
    }

    public void setContentStyle(StyleProperty contentStyle) {
        this.contentStyle = contentStyle;
    }

    public WorkbookType getWorkBookType() {
        return workBookType;
    }

    public void setWorkBookType(WorkbookType workBookType) {
        this.workBookType = workBookType;
    }

    public double getAutoWidthRatio() {
        return autoWidthRatio;
    }

    public void setAutoWidthRatio(double autoWidthRatio) {
        this.autoWidthRatio = autoWidthRatio;
    }

//    public PropertyAccessor getPropertyAccessor() {
//        return propertyAccessor;
//    }
//
//    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
//        this.propertyAccessor = propertyAccessor;
//    }
}
