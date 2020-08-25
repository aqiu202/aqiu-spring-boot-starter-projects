package com.github.aqiu202.autolog.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <pre>自定义的自动生成日志的注解</pre>
 * @author aqiu 2018年10月24日 下午3:53:19
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface AutoLog {

    /**
     * 自定义日志描述模板
     * @return 日志描述
     */
    String value() default "";

}
