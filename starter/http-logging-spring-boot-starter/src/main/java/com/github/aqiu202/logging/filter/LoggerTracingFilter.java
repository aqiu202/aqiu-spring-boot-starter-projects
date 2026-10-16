package com.github.aqiu202.logging.filter;

import com.github.aqiu202.util.RandomCodeUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <pre>日志traceId参数设置，方便通过日志追踪问题</pre>
 *
 * @author aqiu 2020/8/19 11:12 上午
 **/
public class LoggerTracingFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";

    public static final String HEADER_TRACE_ID = "Request-Trace-ID";

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
            String serialNo = this.generateSerialNo();
            MDC.put(TRACE_ID_KEY, serialNo);
            response.setHeader(HEADER_TRACE_ID, serialNo);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }

    protected String generateSerialNo() {
        return RandomCodeUtils.generateSecurityCode(8, RandomCodeUtils.RandomCodeLevel.Medium);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/actuator")
                || requestURI.startsWith("/static")
                || requestURI.startsWith("/error");
    }

}
