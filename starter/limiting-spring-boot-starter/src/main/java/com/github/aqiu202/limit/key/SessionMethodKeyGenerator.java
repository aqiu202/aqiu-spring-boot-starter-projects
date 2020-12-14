package com.github.aqiu202.limit.key;

import com.github.aqiu202.aop.keygen.impl.MethodKeyGenerator;
import com.github.aqiu202.util.ServletRequestUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

/**
 * <pre>{@link SessionMethodKeyGenerator}</pre>
 *
 * @author aqiu 2020/12/14 11:12
 **/
public class SessionMethodKeyGenerator extends MethodKeyGenerator {

    @Override
    public String generate(Object target, Method method, Object... params) {
        final HttpServletRequest request = ServletRequestUtils
                .getCurrentRequest();
        return super.generate(target, method, params) + ":" + (request == null ? "null"
                : request.getSession().getId());
    }
}
