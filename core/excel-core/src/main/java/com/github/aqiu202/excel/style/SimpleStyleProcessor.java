package com.github.aqiu202.excel.style;

import org.apache.poi.common.Duplicatable;
import org.apache.poi.ss.usermodel.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SimpleStyleProcessor implements StyleProcessor {
    private final Workbook workbook;

    private CellStyle baseCellStyle;

    private final Map<StyleProperty, CellStyle> colStyleMap = new ConcurrentHashMap<>();
    private final FontProcessor fontProcessor;

    public SimpleStyleProcessor(Workbook workbook) {
        this.workbook = workbook;
        this.fontProcessor = new SimpleFontProcessor(workbook);
    }

    @Override
    public CellStyle createCellStyle() {
        if (this.baseCellStyle == null) {
            this.baseCellStyle = this.workbook.createCellStyle();
        }
        // 默认使用拷贝方式创建样式
        if (this.baseCellStyle instanceof Duplicatable) {
            return (CellStyle) ((Duplicatable) this.baseCellStyle).copy();
        }
        return this.workbook.createCellStyle();
    }

    @Override
    public CellStyle buildCellStyle(StyleProperty property) {
        if (property == null) {
            return null;
        }
        return this.colStyleMap.compute(property, (k, v) -> {
            if (v == null) {
                v = this.createCellStyle();
                property.apply(v);
                FontProperty fontProperty = property.getFont();
                if (fontProperty != null) {
                    Font font = this.fontProcessor.buildFont(fontProperty);
                    v.setFont(font);
                }
            }
            return v;
        });
    }

    @Override
    public void applyCellStyle(CellStyle style, StyleProperty property) {
        if (style == null || property == null) {
            return;
        }
        property.apply(style);
        FontProperty font = property.getFont();
        if (font != null) {
            style.setFont(this.fontProcessor.buildFont(font));
        }
    }

    @Override
    public StyleProperty getStyleProperty(CellStyle style) {
        if (style == null) {
            return null;
        }
        StyleProperty styleProperty = new StyleProperty();
        styleProperty.from(style);
        return styleProperty;
    }

    @Override
    public CreationHelper getCreationHelper() {
        return this.workbook.getCreationHelper();
    }

    @Override
    public void modifyCellStyle(Cell cell, Function<StyleProperty, StyleProperty> styleModifier) {
        StyleProperty styleProperty = this.getStyleProperty(cell.getCellStyle());
        if (styleModifier != null) {
            StyleProperty modifyProperty = styleModifier.apply(styleProperty);
            if (modifyProperty != null) {
                cell.setCellStyle(this.buildCellStyle(modifyProperty));
            }
        }
    }
}
