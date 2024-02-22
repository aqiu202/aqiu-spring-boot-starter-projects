package com.github.aqiu202.excel.anno;

import com.github.aqiu202.excel.format.*;

import java.lang.annotation.*;

@Documented
@Repeatable(ExcelColumns.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
//excel表格注解
public @interface ExcelColumn {
    //字段excel表头名称
    String value() default "";

    // 默认字段name，可用于JPA关联实体数据
    String field() default "";

    // 列顺序
    int order() default 0;

    //是否为日期格式
    String dateFormat() default "";

    Class<? extends DateFormatter> dateFormatter() default SimpleDateFormatter.class;

    String numberFormat() default "";

    Class<? extends NumberFormatter> numberFormatter() default DecimalFormatter.class;

    Class<? extends NullFormatter> nullFormatter() default SimpleNullFormatter.class;

    String converter() default "";
}
