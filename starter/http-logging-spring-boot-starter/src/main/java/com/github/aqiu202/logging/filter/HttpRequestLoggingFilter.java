package com.github.aqiu202.logging.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HttpRequestLoggingFilter extends CommonsRequestLoggingFilter {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private List<String> whitelist = Collections.emptyList();

    private List<String> blacklist = Collections.emptyList();

    public void setWhitelist(Collection<String> whitelist) {
        this.whitelist = whitelist == null ? Collections.emptyList() : List.copyOf(whitelist);
    }

    public void setBlacklist(Collection<String> blacklist) {
        this.blacklist = blacklist == null ? Collections.emptyList() : List.copyOf(blacklist);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String path = requestUri.substring(request.getContextPath().length());
        return matches(blacklist, path) || (!whitelist.isEmpty() && !matches(whitelist, path));
    }

    private boolean matches(Collection<String> patterns, String path) {
        return patterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
