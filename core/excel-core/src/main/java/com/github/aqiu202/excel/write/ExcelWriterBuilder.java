package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.style.StyleProperty;

public abstract class ExcelWriterBuilder {

    protected SheetWriteConfiguration configuration = new SheetWriteConfiguration();

    public ExcelWriterBuilder configuration(SheetWriteConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public ExcelWriterBuilder setRowAccessWindowSize(int rowAccessWindowSize) {
        if (this.configuration != null) {
            this.configuration.setRowAccessWindowSize(rowAccessWindowSize);
        }
        return this;
    }

    public ExcelWriterBuilder setWorkBookType(WorkbookType workBookType) {
        if (this.configuration != null) {
            this.configuration.setWorkBookType(workBookType);
        }
        return this;
    }

    public ExcelWriterBuilder setAutoWidthRatio(double autoWidthRatio) {
        if (this.configuration != null) {
            this.configuration.setAutoWidthRatio(autoWidthRatio);
        }
        return this;
    }

    public ExcelWriterBuilder setHeadStyle(StyleProperty headStyle) {
        if (this.configuration != null) {
            this.configuration.setHeadStyle(headStyle);
        }
        return this;
    }

    public ExcelWriterBuilder setContentStyle(StyleProperty contentStyle) {
        if (this.configuration != null) {
            this.configuration.setContentStyle(contentStyle);
        }
        return this;
    }

    public ExcelWriterBuilder setAutoSizeColumn(boolean autoSizeColumn) {
        if (this.configuration != null) {
            this.configuration.setAutoSizeColumn(autoSizeColumn);
        }
        return this;
    }

    public SheetWriteConfiguration getConfiguration() {
        return configuration;
    }

    public abstract ExcelWriter build();
}
