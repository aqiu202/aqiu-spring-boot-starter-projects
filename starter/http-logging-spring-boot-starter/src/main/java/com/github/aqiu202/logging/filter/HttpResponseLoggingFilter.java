package com.github.aqiu202.logging.filter;

import com.github.aqiu202.util.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        if (this.isSseRequest(request)) {
            filterChain.doFilter(request, response);
            logger.debug("Response: [SSE stream, skip logging]");
        } else {
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(request, responseWrapper);
            String contentType = responseWrapper.getContentType();
            if (StringUtils.isBlank(contentType) || this.isValidAndCompatibleMediaType(contentType, MediaType.APPLICATION_JSON)) {
                byte[] data = responseWrapper.getContentAsByteArray();
                logger.debug("Response: " + new String(data, StandardCharsets.UTF_8));
            } else {
                logger.debug("Response: [unsupported display]");
            }
            responseWrapper.copyBodyToResponse();
            logger.debug("Response Headers: " + this.readHeaderAsMap(responseWrapper));
        }
    }

    private boolean isSseRequest(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return StringUtils.isNotBlank(accept)
                && StringUtils.equalsAnyIgnoreCase(accept, MediaType.TEXT_EVENT_STREAM_VALUE);
    }

    private boolean isValidAndCompatibleMediaType(String text,
                                                  @Nonnull MediaType mediaType) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        MediaType sourceMediaType = this.parseMediaType(text);
        return sourceMediaType != null && sourceMediaType.isCompatibleWith(mediaType);
    }

    @Nullable
    private MediaType parseMediaType(String mediaTypeText) {
        try {
            return MediaType.parseMediaType(mediaTypeText);
        } catch (Exception e) {
            logger.error("MediaType解析异常", e);
            return null;
        }
    }

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return !logger.isDebugEnabled();
    }

    protected Map<String, List<String>> readHeaderAsMap(HttpServletResponse response) {
        Map<String, List<String>> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, new ArrayList<>(response.getHeaders(headerName)));
        }
        return headers;
    }

}
