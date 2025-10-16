package com.github.aqiu202.excel.anno;

import com.github.aqiu202.excel.model.PropertyAccessor;
import com.github.aqiu202.excel.format.*;

import java.lang.annotation.*;

@Documented
@Repeatable(ExcelColumns.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
//excel表格注解
public @interface ExcelColumn {

    //字段excel表头名称
    String[] value();

    // 默认字段name
    String field() default "";

    // 列顺序
    int order() default 0;

    // 公式
    String formula() default "";

    //是否为图片
    boolean image() default false;

    /**
     * 列宽
     */
    int width() default 0;

    boolean enableDefaultFormatter() default true;

    //是否为日期格式
    String dateFormat() default "";

    Class<? extends DateFormatter> dateFormatter() default SimpleDateFormatter.class;

    String numberFormat() default "";

    Class<? extends NumberFormatter> numberFormatter() default DecimalFormatter.class;

    Class<? extends NullFormatter> nullFormatter() default SimpleNullFormatter.class;

    String converter() default "";

    /**
     * @see PropertyAccessor#name()
     */
    String accessor() default "";
}
