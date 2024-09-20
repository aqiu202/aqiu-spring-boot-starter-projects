package com.github.aqiu202.autolog.aop;

import com.github.aqiu202.autolog.interceptor.ParamReader;
import com.github.aqiu202.util.StringUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public interface LogMethodParamProcessor {

    ParamReader getParamReader();

    ParameterNameDiscoverer getParameterNameDiscoverer();

    /**
     * <pre>根据方法参数生成描述</pre>
     * @param method 方法
     * @param args 参数
     * @return 自动生成的日志描述信息
     * @author aqiu
     **/
    default String handleParams(Method method, Object[] args) {
        String[] paramNames = this.getParameterNameDiscoverer().getParameterNames(method);
        Assert.notNull(paramNames, "获取方法参数名称失败");
        StringJoiner joiner = new StringJoiner(",", "请求参数：[", "]");
        Map<String, Object> paramMap = new HashMap<>();
        ParamReader paramReader = this.getParamReader();
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            if (o instanceof ServletRequest || o instanceof ServletResponse) {
                continue;
            }
            String value = paramNames[i] + ":{" + i + "}";
            joiner.add(value);
            paramMap.put(String.valueOf(i), paramReader.apply(o));
        }
        return StringUtils.stringFormat(joiner.toString(), paramMap).replace("[", "{")
                .replace("]", "}");
    }
}
