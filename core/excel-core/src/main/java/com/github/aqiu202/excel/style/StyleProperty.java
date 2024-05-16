package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Objects;

public class StyleProperty {
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
    private Short dataFormat;
    private Boolean locked;

    @NestedConfigurationProperty
    private FontProperty font = new FontProperty();

    @NestedConfigurationProperty
    private BorderProperty border = new BorderProperty();

    @NestedConfigurationProperty
    private ColorProperty background = new ColorProperty();

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public StyleProperty setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public StyleProperty setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    public FontProperty getFont() {
        return font;
    }

    public StyleProperty setFont(FontProperty font) {
        this.font = font;
        return this;
    }

    public BorderProperty getBorder() {
        return border;
    }

    public StyleProperty setBorder(BorderProperty border) {
        this.border = border;
        return this;
    }

    public ColorProperty getBackground() {
        return background;
    }

    public StyleProperty setBackground(ColorProperty background) {
        this.background = background;
        return this;
    }

    public Short getDataFormat() {
        return dataFormat;
    }

    public StyleProperty setDataFormat(Short dataFormat) {
        this.dataFormat = dataFormat;
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public StyleProperty setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public void apply(CellStyle cellStyle, Font font) {
        if (font != null) {
            this.font.apply(font);
        }
        this.apply(cellStyle);
    }

    public void apply(CellStyle cellStyle) {
        if (cellStyle != null) {
            cellStyle.setAlignment(this.getAlignment());
            cellStyle.setVerticalAlignment(this.getVerticalAlignment());
            if (this.background != null) {
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setFillForegroundColor(this.background.resolveColorIndex());
            }
            if (this.border != null) {
                this.border.apply(cellStyle);
            }
            if (this.dataFormat != null) {
                cellStyle.setDataFormat(this.dataFormat);
            }
            if (this.locked != null) {
                cellStyle.setLocked(this.locked);
            }
        }
    }

    public void from(CellStyle cellStyle) {
        if (cellStyle != null) {
            this.setAlignment(cellStyle.getAlignment());
            this.setVerticalAlignment(cellStyle.getVerticalAlignment());
            if (this.background != null) {
                this.background.setIndex(cellStyle.getFillForegroundColor());
            }
            if (this.border != null) {
                this.border.from(cellStyle);
            }
            this.dataFormat = cellStyle.getDataFormat();
            this.locked = cellStyle.getLocked();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleProperty that = (StyleProperty) o;
        return alignment == that.alignment && verticalAlignment == that.verticalAlignment && Objects.equals(dataFormat, that.dataFormat) && Objects.equals(font, that.font) && Objects.equals(border, that.border) && Objects.equals(background, that.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alignment, verticalAlignment, dataFormat, font, border, background);
    }
}
