package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleFontProcessor implements FontProcessor {

    private final Map<FontProperty, Font> fontMap = new ConcurrentHashMap<>();
    private final Workbook workbook;


    public SimpleFontProcessor(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public Font buildFont(FontProperty property) {
        if (property == null) {
            return null;
        }
        return this.fontMap.compute(property, (k, v) -> {
            if (v == null) {
                v = this.workbook.createFont();
                property.apply(v);
            }
            return v;
        });
    }

    @Override
    public FontProperty getFontProperty(Font font) {
        if (font == null) {
            return null;
        }
        FontProperty fontProperty = new FontProperty();
        fontProperty.from(font);
        return fontProperty;
    }
}
