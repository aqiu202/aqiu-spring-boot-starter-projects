package com.github.aqiu202.excel.style;

import org.apache.poi.common.Duplicatable;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleExcelStyleBuilder implements ExcelStyleBuilder {
    private final Workbook workbook;

    private CellStyle baseCellStyle;

    private final Map<StyleProperty, CellStyle> styleMap = new ConcurrentHashMap<>();

    public SimpleExcelStyleBuilder(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public Font buildFont(FontProperty property) {
        if (property == null) {
            return null;
        }
        Font font = this.workbook.createFont();
        property.apply(font);
        return font;
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
        // 样式缓存
        return this.styleMap.compute(property, (k, v) -> {
            if (v == null) {
                v = this.createCellStyle();
                property.apply(v);
                FontProperty font = property.getFont();
                if (font != null) {
                    v.setFont(this.buildFont(font));
                }
            }
            return v;
        });
    }

    @Override
    public CreationHelper getCreationHelper() {
        return this.workbook.getCreationHelper();
    }
}
