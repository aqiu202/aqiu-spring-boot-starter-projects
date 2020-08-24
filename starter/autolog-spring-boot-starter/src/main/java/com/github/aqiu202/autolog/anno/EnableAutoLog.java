package com.github.aqiu202.autolog.anno;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.aqiu202.autolog.config.AutoLogConfigurationSelector;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * <pre>开启AutoLog注解的aop功能</pre>
 * @author aqiu 2018年10月24日 下午3:52:22
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Import(AutoLogConfigurationSelector.class)
public @interface EnableAutoLog {

    boolean enable() default true;

    boolean debug() default false;
}
