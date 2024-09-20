package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.MetaAnalyzer;
import com.github.aqiu202.excel.convert.*;
import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.format.wrap.ImageValueWrapper;
import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.write.Heads;
import com.github.aqiu202.util.StringUtils;

import java.util.List;

/**
 * <b>Excel导入核心操作封装</b>
 *
 * @author xuqiu 2023/3/22 14:08
 **/
public interface DataExtractor<M extends TableMeta> extends DataConverter<Object, Object> {


    /**
     * 获取元数据解析器
     */
    MetaAnalyzer<M> getMetaAnalyzer();

    /**
     * 提取元数据
     */
    List<M> extractMetas(Class<?> dataType);

    /**
     * 从元数据中提取表头信息
     */
    Heads extractHeadsFromMetas(List<M> metas);

    /**
     * 根据元数据提取原始值
     */
    default Object extractValue(M meta, Object instance) {
        return meta.getValueDescriptor().getValue(instance);
    }

    /**
     * 获取格式化器门面
     */
    default FormatterFacade getFormatterFacade() {
        return SimpleFormatterFacade.INSTANCE;
    }

    /**
     * 获取值转换器，并执行数据转换
     */
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

    /**
     * 从元数据中提取格式化器提供者
     */
    default FormatterProvider extractFormatterProvider(M meta, FormatterProvider global) {
        if (meta instanceof FormatterProviderWrapper) {
            FormatterProvider formatterProvider = ((FormatterProviderWrapper) meta).getProvider();
            if (formatterProvider != null) {
                return formatterProvider;
            }
        }
        return global;
    }

    /**
     * 从元数据中提取值转换器提供者
     */
    default ConverterProvider extractConverterProvider(M meta) {
        if (meta instanceof ConverterProviderWrapper) {
            return ((ConverterProviderWrapper) meta).getConverterProvider();
        }
        return null;
    }

    /**
     * 根据元数据提取数据转换+格式化后的值
     */
    default ValueWrapper<?> extractFormattedValue(M meta, Object instance, FormatterProvider global, ConverterFactory converterFactory) {
        Object value = this.extractValue(meta, instance);
        // 图片无需转换和格式化
        if (meta.isImage()) {
            return new ImageValueWrapper(String.valueOf(value));
        }
        ConverterProvider converterProvider = this.extractConverterProvider(meta);
        if (converterProvider != null) {
            value = this.convertValue(value, converterProvider, converterFactory);
        }
        return this.getFormatterFacade().format(value, this.extractFormatterProvider(meta, global));
    }

}
