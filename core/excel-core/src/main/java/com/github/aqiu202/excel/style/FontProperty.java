package com.github.aqiu202.excel.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Objects;

public class FontProperty {
    private String fontName = "宋体";
    private short fontSize = 10;
    private boolean italic;
    private boolean bold;
    private short fontHeight;
    private int charSet;
    private boolean strikeout;
    private byte underline;

    @NestedConfigurationProperty
    private ColorProperty color = new ColorProperty(HSSFColor.HSSFColorPredefined.BLACK);

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getFontSize() {
        return fontSize;
    }

    public void setFontSize(short fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public short getFontHeight() {
        return fontHeight;
    }

    public void setFontHeight(short fontHeight) {
        this.fontHeight = fontHeight;
    }

    public int getCharSet() {
        return charSet;
    }

    public void setCharSet(int charSet) {
        this.charSet = charSet;
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public byte getUnderline() {
        return underline;
    }

    public void setUnderline(byte underline) {
        this.underline = underline;
    }

    public ColorProperty getColor() {
        return color;
    }

    public void setColor(ColorProperty color) {
        this.color = color;
    }

    public void from(Font font) {
        if (font != null) {
            this.setFontName(font.getFontName());
            this.setFontSize(font.getFontHeightInPoints());
            this.setItalic(font.getItalic());
            this.setBold(font.getBold());
            this.setFontHeight(font.getFontHeight());
            this.setCharSet(font.getCharSet());
            this.setStrikeout(font.getStrikeout());
            this.setUnderline(font.getUnderline());
            this.color.setIndex(font.getColor());
        }
    }

    public void apply(Font font) {
        if (font != null) {
            font.setUnderline(this.getUnderline());
            font.setFontHeight(this.getFontHeight());
            font.setCharSet(this.getCharSet());
            font.setStrikeout(this.isStrikeout());
            font.setFontName(this.getFontName());
            font.setFontHeightInPoints(this.getFontSize());
            font.setBold(this.isBold());
            font.setItalic(this.isItalic());
            font.setColor(this.color.resolveColorIndex());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontProperty that = (FontProperty) o;
        return fontSize == that.fontSize && italic == that.italic && bold == that.bold && fontHeight == that.fontHeight && charSet == that.charSet && strikeout == that.strikeout && underline == that.underline && Objects.equals(fontName, that.fontName) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontName, fontSize, italic, bold, fontHeight, charSet, strikeout, underline, color);
    }
}
