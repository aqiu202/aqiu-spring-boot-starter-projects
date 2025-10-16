package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookType;
import com.github.aqiu202.excel.style.StyleProperty;

import java.util.function.Consumer;

public abstract class ExcelWriterBuilder {

    protected WorkbookSheetWriteConfiguration configuration = new WorkbookSheetWriteConfiguration();
    protected ConverterFactory converterFactory;
    protected ExcelBeforeExportHandler beforeExportHandler;

    protected ExcelWriterBuilder(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public ExcelWriterBuilder configuration(WorkbookSheetWriteConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public ExcelWriterBuilder converterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

    public ExcelWriterBuilder configuration(Consumer<WorkbookSheetWriteConfiguration> configurationConsumer) {
        configurationConsumer.accept(this.configuration);
        return this;
    }

    public ExcelWriterBuilder rowAccessWindowSize(int rowAccessWindowSize) {
        this.configuration.setRowAccessWindowSize(rowAccessWindowSize);
        return this;
    }

    public ExcelWriterBuilder workBookType(WorkbookType workBookType) {
        this.configuration.setWorkBookType(workBookType);
        return this;
    }

    public ExcelWriterBuilder autoWidthRatio(double autoWidthRatio) {
        this.configuration.setAutoWidthRatio(autoWidthRatio);
        return this;
    }

    public ExcelWriterBuilder headStyle(StyleProperty headStyle) {
        this.configuration.setHeadStyle(headStyle);
        return this;
    }

    public ExcelWriterBuilder headStyle(Consumer<StyleProperty> consumer) {
        if (consumer != null) {
            StyleProperty styleProperty = this.configuration.getHeadStyle();
            if (styleProperty == null) {
                styleProperty = new StyleProperty();
                this.configuration.setHeadStyle(styleProperty);
            }
            consumer.accept(styleProperty);
        }
        return this;
    }

    public ExcelWriterBuilder contentStyle(StyleProperty contentStyle) {
        this.configuration.setContentStyle(contentStyle);
        return this;
    }

    public ExcelWriterBuilder contentStyle(Consumer<StyleProperty> consumer) {
        if (consumer != null) {
            StyleProperty styleProperty = this.configuration.getContentStyle();
            if (styleProperty == null) {
                styleProperty = new StyleProperty();
                this.configuration.setContentStyle(styleProperty);
            }
            consumer.accept(styleProperty);
        }
        return this;
    }

    public ExcelWriterBuilder autoSizeColumn(boolean autoSizeColumn) {
        this.configuration.setAutoSizeColumn(autoSizeColumn);
        return this;
    }

    public ExcelWriterBuilder enableAutoSizeColumn() {
        return this.autoSizeColumn(true);
    }

    public ExcelWriterBuilder disableAutoSizeColumn() {
        return this.autoSizeColumn(false);
    }

    public ExcelWriterBuilder beforeExportHandler(ExcelBeforeExportHandler beforeExportHandler) {
        this.beforeExportHandler = beforeExportHandler;
        return this;
    }

    public abstract ExcelWriter build();
}
