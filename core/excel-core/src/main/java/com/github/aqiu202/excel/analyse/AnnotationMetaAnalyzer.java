package com.github.aqiu202.excel.analyse;

import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.meta.ExcelFieldMeta;
import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationMetaAnalyzer implements MetaAnalyzer<ExcelFieldMeta> {

    /**
     * 严格模式，如果为true，则只分析有注解的字段
     */
    private boolean strict = true;
    private PropertyAccessor propertyAccessor = PropertyAccessor.FIELD;

    public AnnotationMetaAnalyzer() {
    }

    public AnnotationMetaAnalyzer(boolean strict, PropertyAccessor propertyAccessor) {
        this.strict = strict;
        this.propertyAccessor = propertyAccessor;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }

    public boolean isStrict() {
        return strict;
    }

    public PropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }

    @Override
    public List<ExcelFieldMeta> analyseMetas(Class<?> type) {
        if (!ClassUtils.isCustomClass(type)) {
            return Collections.emptyList();
        }
        return ReflectionUtils.getAllField(type).stream()
                .filter(field -> !this.isStrict() || ReflectionUtils.hasAnnotation(field, ExcelColumn.class))
                .flatMap(field -> {
                    List<ExcelColumn> annotations = ReflectionUtils.getAnnotations(field, ExcelColumn.class);
                    if (annotations.isEmpty()) {
                        return this.isStrict() ?
                                Stream.empty():
                                Stream.of(this.buildWithField(field));
                    } else {
                        return annotations.stream()
                                .map(annotation -> this.buildWithAnnotation(field, annotation));
                    }
                }).sorted(Comparator.comparing(ExcelFieldMeta::getOrder)).collect(Collectors.toList());
    }

    protected ExcelFieldMeta buildWithField(Field field) {
        return new ExcelFieldMeta(field, this.getPropertyAccessor());
    }

    protected ExcelFieldMeta buildWithAnnotation(Field field, ExcelColumn annotation) {
        return new ExcelFieldMeta(field, annotation, this.getPropertyAccessor());
    }

}
