package com.github.aqiu202.autolog.interceptor;

import com.github.aqiu202.autolog.anno.AutoLog;
import com.github.aqiu202.autolog.result.LogRequestParam;
import javax.servlet.http.HttpServletRequest;

/**
 * <pre>日志采集器</pre>
 * @author aqiu 2019/12/7 12:30 下午
 **/
public interface LogCollector {

    LogRequestParam collect(HttpServletRequest request, AutoLog autoLog, Throwable throwable,
            Object... args);
}
