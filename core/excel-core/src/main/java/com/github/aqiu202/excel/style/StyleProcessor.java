package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;

import java.util.function.Function;

public interface StyleProcessor {

    CellStyle createCellStyle();

    CellStyle buildCellStyle(StyleProperty property);

    void applyCellStyle(CellStyle style, StyleProperty property);

    StyleProperty getStyleProperty(CellStyle style);

    CreationHelper getCreationHelper();

    void modifyCellStyle(Cell cell, Function<StyleProperty, StyleProperty> styleModifier);

}
