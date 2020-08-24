package com.github.aqiu202.autolog.interceptor;


import com.github.aqiu202.autolog.result.LogRequestParam;

/**
 * <pre>日志处理 对所有的AutoLog方法生成的日志进行处理</pre>
 * @author aqiu 2018年10月24日 下午3:55:28
 */
public interface LogHandler {

    void handle(LogRequestParam logRequestParam);
}
