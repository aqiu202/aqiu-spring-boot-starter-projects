package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.Font;

public interface FontProcessor {

    Font buildFont(FontProperty property);

    FontProperty getFontProperty(Font font);
}
