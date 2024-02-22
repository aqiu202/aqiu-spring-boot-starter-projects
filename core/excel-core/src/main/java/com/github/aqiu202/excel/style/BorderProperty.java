package com.github.aqiu202.excel.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class BorderProperty {

    private BorderStyle left = BorderStyle.THIN;
    private BorderStyle right = BorderStyle.THIN;
    private BorderStyle top = BorderStyle.THIN;
    private BorderStyle bottom = BorderStyle.THIN;
    @NestedConfigurationProperty
    private ColorProperty leftColor = new ColorProperty(HSSFColor.HSSFColorPredefined.BLACK);
    @NestedConfigurationProperty
    private ColorProperty rightColor = new ColorProperty(HSSFColor.HSSFColorPredefined.BLACK);
    @NestedConfigurationProperty
    private ColorProperty topColor = new ColorProperty(HSSFColor.HSSFColorPredefined.BLACK);
    @NestedConfigurationProperty
    private ColorProperty bottomColor = new ColorProperty(HSSFColor.HSSFColorPredefined.BLACK);

    public BorderStyle getLeft() {
        return left;
    }

    public void setLeft(BorderStyle left) {
        this.left = left;
    }

    public BorderStyle getRight() {
        return right;
    }

    public void setRight(BorderStyle right) {
        this.right = right;
    }

    public BorderStyle getTop() {
        return top;
    }

    public void setTop(BorderStyle top) {
        this.top = top;
    }

    public BorderStyle getBottom() {
        return bottom;
    }

    public void setBottom(BorderStyle bottom) {
        this.bottom = bottom;
    }

    public ColorProperty getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(ColorProperty leftColor) {
        this.leftColor = leftColor;
    }

    public ColorProperty getRightColor() {
        return rightColor;
    }

    public void setRightColor(ColorProperty rightColor) {
        this.rightColor = rightColor;
    }

    public ColorProperty getTopColor() {
        return topColor;
    }

    public void setTopColor(ColorProperty topColor) {
        this.topColor = topColor;
    }

    public ColorProperty getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(ColorProperty bottomColor) {
        this.bottomColor = bottomColor;
    }

    public void apply(CellStyle cellStyle) {
        cellStyle.setBorderLeft(this.getLeft());
        cellStyle.setBorderRight(this.getRight());
        cellStyle.setBorderTop(this.getTop());
        cellStyle.setBorderBottom(this.getBottom());
        cellStyle.setLeftBorderColor(this.leftColor.resolveColorIndex());
        cellStyle.setRightBorderColor(this.rightColor.resolveColorIndex());
        cellStyle.setTopBorderColor(this.topColor.resolveColorIndex());
        cellStyle.setBottomBorderColor(this.bottomColor.resolveColorIndex());
    }
}
