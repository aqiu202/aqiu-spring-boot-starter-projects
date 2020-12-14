package com.github.aqiu202.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <pre>ServletRequest工具类</pre>
 *
 * @author aqiu 2020/11/29 1:52
 **/
public abstract class ServletRequestUtils {

    @Nullable
    public static HttpServletRequest getCurrentRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
