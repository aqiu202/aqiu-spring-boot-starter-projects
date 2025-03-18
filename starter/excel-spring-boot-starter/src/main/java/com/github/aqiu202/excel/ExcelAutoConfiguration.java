package com.github.aqiu202.excel;

import com.github.aqiu202.excel.convert.ConverterFactory;
import com.github.aqiu202.excel.convert.NamedConverter;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.model.WorkbookSheetWriteConfiguration;
import com.github.aqiu202.excel.read.ExcelReader;
import com.github.aqiu202.excel.write.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ExcelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SheetWriteConfiguration.class)
    @ConfigurationProperties(prefix = "excel.write")
    public WorkbookSheetWriteConfiguration workbookWriteConfiguration() {
        return new WorkbookSheetWriteConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(SheetReadConfiguration.class)
    @ConfigurationProperties(prefix = "excel.read")
    public SheetReadConfiguration sheetReadConfiguration() {
        return new SheetReadConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(ExcelFactory.class)
    public ExcelFactory excelFactory(@Autowired(required = false) ConverterFactory converterFactory,
                                     List<NamedConverter<?, ?>> converters) {
        if (converterFactory == null) {
            converterFactory = new SimpleConverterFactory();
        }
        converters.forEach(converterFactory::addConverter);
        return new SimpleExcelFactory().converterFactory(converterFactory);
    }

    @Bean
    @ConditionalOnMissingBean(ExcelWriter.class)
    public ExcelWriter excelWriter(ExcelFactory excelFactory, WorkbookSheetWriteConfiguration writeConfiguration) {
        return excelFactory.buildWriter().configuration(writeConfiguration).build();
    }

    @Bean
    @ConditionalOnMissingBean(ExcelReader.class)
    public ExcelReader excelReader(ExcelFactory excelFactory, SheetReadConfiguration readConfiguration) {
        return excelFactory.buildReader().configuration(readConfiguration).build();
    }
}
