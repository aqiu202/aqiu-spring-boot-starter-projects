package com.github.aqiu202.autolog.interceptor;

import java.util.function.Function;

/**
 * <pre>对参数的读取（仅在@AutoLog没有填写value值的时候使用）</pre>
 * @author aqiu 2018年10月24日 下午3:55:28
 */
@FunctionalInterface
public interface ParamReader extends Function<Object, String> {
}
