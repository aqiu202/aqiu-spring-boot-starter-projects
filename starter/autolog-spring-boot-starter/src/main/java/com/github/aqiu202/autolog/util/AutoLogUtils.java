package com.github.aqiu202.autolog.util;

import com.github.aqiu202.autolog.result.LogRequestParam;
import com.github.aqiu202.autolog.result.LogRequestParam.DefaultLogRequestParam;
import com.github.aqiu202.util.IPUtils;
import com.github.aqiu202.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AutoLogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLogUtils.class);

    public static final boolean LOG_RESULT_SUCCESS = true;
    public static final boolean LOG_RESULT_FAil = false;

    public static LogRequestParam collect(HttpServletRequest request, String desc) {
        LogRequestParam param = new DefaultLogRequestParam();
        String context = request.getContextPath();
        String uri = request.getRequestURI();
        // url
        uri = uri.replace(context, "");
        // method
        String method = request.getMethod();
        // ip
        String ip = IPUtils.getIpAddress(request);
        LOGGER.info("url={}", uri);
        LOGGER.info("method={}", method);
        LOGGER.info("ip={}", ip);
        LOGGER.info("desc={}", desc);
        param.setIp(ip);
        param.setMethod(method);
        param.setUri(uri);
        param.setDesc(desc);
        return param;
    }

    public static void handleDefaultValues(HttpServletRequest request, LogRequestParam param, Throwable throwable) {
        //日志信息未填充，会使用默认日志采集器填充
        if (StringUtils.isEmpty(param.getIp())) {
            param.setIp(IPUtils.getIpAddress(request));
        }
        if (StringUtils.isEmpty(param.getUri())) {
            param.setUri(request.getRequestURI().replace(request.getContextPath(), ""));
        }
        if (StringUtils.isEmpty(param.getMethod())) {
            param.setMethod(request.getMethod());
        }
        if (param.getResult() == null) {
            if (throwable == null) {
                param.setResult(LOG_RESULT_SUCCESS);
            } else {
                param.setResult(LOG_RESULT_FAil);
            }
        }
    }
}
