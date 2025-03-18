package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.model.SheetReadConfiguration;

import java.util.function.Consumer;

public abstract class ExcelReaderBuilder {

    protected SheetReadConfiguration configuration = new SheetReadConfiguration();
    protected ConverterFactory converterFactory;

    public ExcelReaderBuilder(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public ExcelReaderBuilder configuration(SheetReadConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public ExcelReaderBuilder configuration(Consumer<SheetReadConfiguration> configurationConsumer) {
        configurationConsumer.accept(this.configuration);
        return this;
    }

    public ExcelReaderBuilder converterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

    public ExcelReaderBuilder readFormula(boolean readFormula) {
        this.configuration.setReadFormula(readFormula);
        return this;
    }

    public ExcelReaderBuilder enableReadFormula() {
        return this.readFormula(true);
    }

    public ExcelReaderBuilder disableReadFormula() {
        return this.readFormula(false);
    }

    public ExcelReaderBuilder readEmptyText(boolean readEmptyText) {
        this.configuration.setReadEmptyText(readEmptyText);
        return this;
    }

    public ExcelReaderBuilder enableReadEmptyText() {
        return this.readEmptyText(true);
    }

    public ExcelReaderBuilder disableReadEmptyText() {
        return this.readEmptyText(false);
    }

    public abstract ExcelReader build();
}
