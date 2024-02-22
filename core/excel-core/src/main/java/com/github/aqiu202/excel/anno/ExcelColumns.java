package com.github.aqiu202.excel.anno;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumns {

    ExcelColumn[] value() default {};

}
