package com.github.aqiu202.excel.style;

import java.util.Objects;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;

public class ColorProperty {

    public ColorProperty(HSSFColorPredefined name) {
        this.name = name;
    }

    /**
     * 默认颜色（白色）
     */
    public ColorProperty() {
        this((short)0x09);
    }

    public ColorProperty(short index) {
        this.index = index;
    }

    private short index;
    private HSSFColorPredefined name;

    public short getIndex() {
        return index;
    }

    public void setIndex(short index) {
        this.index = index;
    }

    public HSSFColorPredefined getName() {
        return name;
    }

    public void setName(HSSFColorPredefined name) {
        this.name = name;
    }

    public short resolveColorIndex() {
        short result;
        HSSFColorPredefined colorPredefined = this.getName();
        if (Objects.nonNull(colorPredefined)) {
            result = colorPredefined.getIndex();
        } else {
            result = this.getIndex();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorProperty that = (ColorProperty) o;
        return index == that.index && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name);
    }
}
