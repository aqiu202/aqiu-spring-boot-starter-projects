package com.github.aqiu202.autolog.aop;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <pre>Debug模式过滤器，用于日志打印所有请求参数，方便在调试和对接阶段清楚的找到问题</pre>
 *
 * @author aqiu 2020/8/19 11:12 上午
 **/
public class DebugLogFilter extends OncePerRequestFilter {

    private static final String LOG_PREFIX = "DEBUG-LOG | ";

    private final Logger log = LoggerFactory.getLogger(DebugLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String query = request.getQueryString();
        log.info("{}HTTP: Query: {}", LOG_PREFIX, query);
        String method = request.getMethod();
        log.info("{}HTTP: Method: {}", LOG_PREFIX, method);
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        log.info("{}HTTP: Path: {}", LOG_PREFIX, uri);
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, List<String>> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, enumerationToString(request.getHeaders(headerName)));
        }
        log.info("{}HTTP: Headers: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF, headers);
        LogHttpServletRequestWrapper requestWrapper = null;
        switch (HttpMethod.valueOf(method)) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                Map<String, String[]> map = request.getParameterMap();
                Map<String, List<String>> parameters = new HashMap<>();
                map.forEach(
                        (key, value) -> parameters.put(key, Arrays.asList(value))
                );
                log.info("{}HTTP: Parameters: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF, parameters);
                requestWrapper = new LogHttpServletRequestWrapper(request);
                log.info("{}HTTP: Body: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
                        requestWrapper.getBody());
                break;
            default:
                break;
        }
        if (requestWrapper != null) {
            filterChain.doFilter(requestWrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/actuator")
                || requestURI.startsWith("/static")
                || requestURI.startsWith("/error");
    }

    protected List<String> enumerationToString(Enumeration<String> enumeration) {
        List<String> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }
}
