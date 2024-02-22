package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.convert.*;
import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.format.wrap.ResultWrapper;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.meta.MetaAnalyzer;
import com.github.aqiu202.util.StringUtils;

/**
 * <b>Excel Sheet数据提取器</b>
 *
 * @author xuqiu 2023/3/22 14:08
 **/
public interface DataExtractor<M extends MappingMeta> extends DataConverter<Object, Object>, MetaAnalyzer<M> {

    Object extractValue(M meta, Object instance);

    default FormatterFacade getFormatterFacade() {
        return SimpleFormatterFacade.INSTANCE;
    }

    @Override
    default Object convertValue(Object target, ConverterProvider converterProvider, ConverterFactory converterFactory) {
        if (converterProvider == null) {
            return target;
        }
        String converterName = converterProvider.getConverter();
        if (StringUtils.isBlank(converterName)) {
            return target;
        }
        Converter converter = converterFactory.findConverter(converterName);
        if (converter == null) {
            return target;
        }
        return converter.convert(target);
    }

    default FormatterProvider extractFormatterProvider(MappingMeta meta, FormatterProvider global) {
        if (meta instanceof FormatterProviderWrapper) {
            FormatterProvider formatterProvider = ((FormatterProviderWrapper) meta).getFormatterProvider();
            if (formatterProvider != null) {
                return formatterProvider;
            }
        }
        return global;
    }

    default ConverterProvider extractConverterProvider(MappingMeta meta) {
        if (meta instanceof ConverterProviderWrapper) {
            return ((ConverterProviderWrapper) meta).getConverterProvider();
        }
        return null;
    }

    default ResultWrapper<?> extractFormattedValue(M meta, Object instance, FormatterProvider global, ConverterFactory converterFactory) {
        Object value = this.extractValue(meta, instance);
        ConverterProvider converterProvider = this.extractConverterProvider(meta);
        if (converterProvider != null) {
            value = this.convertValue(value, converterProvider, converterFactory);
        }
        return this.getFormatterFacade().format(value, this.extractFormatterProvider(meta, global));
    }

}
