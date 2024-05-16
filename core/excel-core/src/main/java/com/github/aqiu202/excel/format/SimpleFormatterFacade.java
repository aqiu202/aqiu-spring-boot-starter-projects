package com.github.aqiu202.excel.format;

import com.github.aqiu202.excel.format.wrap.*;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.StringUtils;

import java.math.BigDecimal;

public class SimpleFormatterFacade implements FormatterFacade {

    public static final FormatterFacade INSTANCE = new SimpleFormatterFacade();
    private FormatterFinder formatterFinder = new SimpleFormatterFinder();

    @Override
    public FormatterFinder getFormatterFinder() {
        return formatterFinder;
    }

    public void setFormatterFinder(FormatterFinder formatterFinder) {
        this.formatterFinder = formatterFinder;
    }

    @Override
    public ValueWrapper<?> format(Object target, FormatterProvider formatterProvider) {
        if (target == null) {
            NullFormatter formatter = this.formatterFinder.findFormatter(formatterProvider.getNullFormatter());
            return new FormatedValueWrapper(null, formatter.format());
        }
        if (ClassUtils.isDate(target)) {
            DateValueWrapper dateWrapper = DateValueWrapper.of(target);
            DateFormatter dateFormatter = this.formatterFinder.findFormatter(formatterProvider.getDateFormatter());
            String pattern = formatterProvider.getDateFormat();
            if (StringUtils.isBlank(pattern)) {
                return dateWrapper;
            }
            return new FormatedValueWrapper(dateWrapper, dateFormatter.format(target, pattern));
        }
        if (ClassUtils.isNumber(target)) {
            NumberFormatter numberFormatter =
                    this.formatterFinder.findFormatter(formatterProvider.getNumberFormatter());
            Number number;
            // 如果是基础类型，使用BigDecimal重新构造
            if (target.getClass().isPrimitive()) {
                number = new BigDecimal(String.valueOf(target));
            } else {
                number = (Number) target;
            }
            NumberValueWrapper numberWrapper = new NumberValueWrapper(number);
            String pattern = formatterProvider.getNumberFormat();
            if (StringUtils.isBlank(pattern)) {
                pattern = FormatterProvider.DEFAULT_NUM_FORMAT_PATTERN;
            }
            return new FormatedValueWrapper(numberWrapper, numberFormatter.format(number, pattern));
        }
        return new StringValueWrapper(String.valueOf(target));
    }

    @Override
    public <T> T parse(String text, Class<T> targetType, FormatterProvider formatterProvider) {
        if (StringUtils.isEmpty(text)) {
            NullFormatter formatter = this.formatterFinder.findFormatter(formatterProvider.getNullFormatter());
            return (T) formatter.parse(targetType);
        }
        if (ClassUtils.isDate(targetType)) {
            DateFormatter dateFormatter = this.formatterFinder.findFormatter(formatterProvider.getDateFormatter());
            String pattern = formatterProvider.getDateFormat();
            if (StringUtils.isBlank(pattern)) {
                pattern = FormatterProvider.DEFAULT_DATE_FORMAT_PATTERN;
            }
            return dateFormatter.parse(text, pattern, targetType);
        }
        if (ClassUtils.isNumber(targetType)) {
            NumberFormatter numberFormatter =
                    this.formatterFinder.findFormatter(formatterProvider.getNumberFormatter());
            String pattern = formatterProvider.getNumberFormat();
            if (StringUtils.isBlank(pattern)) {
                pattern = FormatterProvider.DEFAULT_NUM_FORMAT_PATTERN;
            }
            return numberFormatter.parse(text, pattern, targetType);
        }
        if (String.class.isAssignableFrom(targetType)) {
            return (T) text;
        }
        throw new RuntimeException("不支持的解析类型：" + targetType.getName());
    }

}
