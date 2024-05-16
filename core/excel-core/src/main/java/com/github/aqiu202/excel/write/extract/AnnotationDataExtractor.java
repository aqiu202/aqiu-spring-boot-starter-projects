package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.analyse.AnnotationMetaAnalyzer;
import com.github.aqiu202.excel.meta.ExcelFieldMeta;

public class AnnotationDataExtractor extends AbstractDataExtractor<ExcelFieldMeta> {

    private final AnnotationMetaAnalyzer metaAnalyzer = new AnnotationMetaAnalyzer();

    @Override
    public AnnotationMetaAnalyzer getMetaAnalyzer() {
        return this.metaAnalyzer;
    }

//    @Override
//    public Object extractValue(ExcelFieldMeta annotatedField, Object instance) {
//        Field field = annotatedField.getField();
//        Object result = ReflectionUtils.getValue(instance, field);
//        if (annotatedField.hasAnnotation()) {
//            ExcelColumn annotation = annotatedField.getAnnotation();
//            String fieldName = annotation.field();
//            if (StringUtils.isNotBlank(fieldName)) {
//                String[] fs = fieldName.split("\\.");
//                if (result != null && fs.length > 1) {
//                    String[] properties = Arrays.copyOfRange(fs, 1, fs.length);
//                    result = ReflectionUtils.getValue(result, properties);
//                }
//            }
//        }
//        return result;
//    }
}
