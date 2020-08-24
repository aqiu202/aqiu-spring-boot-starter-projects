package com.github.aqiu202.autolog.aop;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <pre>Debug模式过滤器，用于日志打印所有请求参数，方便在调试和对接阶段清楚的找到问题</pre>
 * @author aqiu 2020/8/19 11:12 上午
**/
public class DebugLogFilter extends OncePerRequestFilter {

    private static final String LOG_PREFIX = "DEBUG-LOG | ";

    private final Logger log = LoggerFactory.getLogger(DebugLogFilter.class);

    private final ObjectWriter objectWriter;

    public DebugLogFilter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("    ",
                DefaultIndenter.SYS_LF);
        pp.indentArraysWith(indenter);
        pp.indentObjectsWith(indenter);
        this.objectWriter = new ObjectMapper().writer(pp);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String query = request.getQueryString();
        log.info("{}Request query: {}", LOG_PREFIX, query);
        String method = request.getMethod();
        log.info("{}Request method: {}", LOG_PREFIX, method);
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        log.info("{}Request path: {}", LOG_PREFIX, uri);
        Map<String, String[]> map = request.getParameterMap();
        log.info("{}Request parameters: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
                objectWriter.writeValueAsString(map));
        LogHttpServletRequestWrapper requestWrapper = null;
        switch (HttpMethod.valueOf(method)) {
            case POST:
            case PUT:
            case PATCH:
                log.info("{}Request Content Type: {}", LOG_PREFIX, request.getContentType());
                requestWrapper = new LogHttpServletRequestWrapper(request);
                log.info("{}Request body: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
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
}
