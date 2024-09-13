package com.github.aqiu202.autolog.aop;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.github.aqiu202.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * <pre>Debug模式过滤器，用于日志打印所有请求参数，方便在调试和对接阶段清楚的找到问题</pre>
 *
 * @author aqiu 2020/8/19 11:12 上午
 **/
public class DebugLoggingFilter extends OncePerRequestFilter {

    private static final String LOG_PREFIX = "SERVLET-LOG | ";

    private final Logger log = LoggerFactory.getLogger(DebugLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String query = request.getQueryString();
        log.info("{}Request--Query: {}", LOG_PREFIX, query);
        String method = request.getMethod();
        log.info("{}Request--Method: {}", LOG_PREFIX, method);
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        log.info("{}Request--Path: {}", LOG_PREFIX, uri);
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, List<String>> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, enumerationToString(request.getHeaders(headerName)));
        }
        log.info("{}Request--Headers: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF, headers);
        LogHttpServletRequestWrapper requestWrapper = null;
        switch (HttpMethod.valueOf(method)) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                String contentType = request.getContentType();
                if (StringUtils.isNotBlank(contentType)) {
                    MediaType mediaType = MediaType.valueOf(contentType);
                    if (mediaType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
                        Map<String, List<String>> files = new HashMap<>();
                        Collection<Part> parts = request.getParts();
                        for (Part part : parts) {
                            String name = part.getName();
                            String value = part.getSubmittedFileName();
                            files.compute(name, (k, v) -> {
                                if (v == null) {
                                    v = new ArrayList<>();
                                }
                                v.add(value);
                                return v;
                            });
                        }
                        log.info("{}Request--Files: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF, files);
                    }
                    requestWrapper = new LogHttpServletRequestWrapper(request);
                    if (mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
                        log.info("{}Request--Body: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
                                requestWrapper.getBody());
                    }
                } else {
                    requestWrapper = new LogHttpServletRequestWrapper(request);
                }
                Map<String, String[]> map = request.getParameterMap();
                Map<String, List<String>> parameters = new HashMap<>();
                map.forEach(
                        (key, value) -> parameters.put(key, Arrays.asList(value))
                );
                log.info("{}Request--Parameters: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF, parameters);
                break;
            default:
                break;
        }
        LogHttpServletResponseWrapper responseWrapper = new LogHttpServletResponseWrapper(response);
        if (requestWrapper != null) {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } else {
            filterChain.doFilter(request, responseWrapper);
        }
        responseWrapper.flushBuffer();
        log.info("{}Response--Status: {}", LOG_PREFIX, response.getStatus());
        log.info("{}Response--Headers: {}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
                responseWrapper.getHeaderMap());
        log.info("{}Response--Content:{}{}", LOG_PREFIX, DefaultIndenter.SYS_LF,
                responseWrapper.getResponseDataAsString());
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
