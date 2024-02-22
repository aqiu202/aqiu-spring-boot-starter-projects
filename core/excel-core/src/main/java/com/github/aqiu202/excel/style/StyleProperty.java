package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Objects;

public class StyleProperty {
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;

    @NestedConfigurationProperty
    private FontProperty font = new FontProperty();

    @NestedConfigurationProperty
    private BorderProperty border = new BorderProperty();

    @NestedConfigurationProperty
    private ColorProperty background = new ColorProperty();

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public FontProperty getFont() {
        return font;
    }

    public void setFont(FontProperty font) {
        this.font = font;
    }

    public BorderProperty getBorder() {
        return border;
    }

    public void setBorder(BorderProperty border) {
        this.border = border;
    }

    public ColorProperty getBackground() {
        return background;
    }

    public void setBackground(ColorProperty background) {
        this.background = background;
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
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleProperty that = (StyleProperty) o;
        return alignment == that.alignment && verticalAlignment == that.verticalAlignment && Objects.equals(font, that.font) && Objects.equals(border, that.border) && Objects.equals(background, that.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alignment, verticalAlignment, font, border, background);
    }
}
