package com.github.aqiu202.excel.analy;

import com.github.aqiu202.excel.anno.ExcelColumn;
import com.github.aqiu202.excel.meta.ExcelColumnField;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationTypeAnalyzer implements TypeAnalyzer<ExcelColumnField> {

    private boolean writeNonAnnotationFields = true;

    public boolean isWriteNonAnnotationFields() {
        return writeNonAnnotationFields;
    }

    public void setWriteNonAnnotationFields(boolean writeNonAnnotationFields) {
        this.writeNonAnnotationFields = writeNonAnnotationFields;
    }

    @Override
    public List<ExcelColumnField> analysis(Class<?> type) {
        if (!ClassUtils.isCustomClass(type)) {
            return Collections.emptyList();
        }
        return ReflectionUtils.getAllField(type).stream()
                .filter(field -> this.isWriteNonAnnotationFields() || ReflectionUtils.hasAnnotation(field, ExcelColumn.class))
                .flatMap(field -> {
                    List<ExcelColumn> annotations = ReflectionUtils.getAnnotations(field, ExcelColumn.class);
                    if (annotations.isEmpty()) {
                        return this.isWriteNonAnnotationFields() ? Stream.of(new ExcelColumnField(field)) :
                                Stream.empty();
                    } else {
                        return annotations.stream().map(annotation -> new ExcelColumnField(field, annotation));
                    }
                }).sorted(Comparator.comparing(ef -> {
                    if (ef.hasAnnotation()) {
                        return ef.getAnnotation().order();
                    }
                    return 0;
                })).collect(Collectors.toList());
    }

}
