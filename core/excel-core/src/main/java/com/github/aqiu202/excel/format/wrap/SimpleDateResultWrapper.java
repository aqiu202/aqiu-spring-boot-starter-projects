package com.github.aqiu202.excel.format.wrap;

import com.github.aqiu202.util.ClassUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SimpleDateResultWrapper implements DateResultWrapper {

    private final LocalDateTime result;

    public SimpleDateResultWrapper(LocalDateTime result) {
        this.result = result;
    }

    public SimpleDateResultWrapper(Date result) {
        this(LocalDateTime.ofInstant((result).toInstant(), ZoneId.systemDefault()));
    }

    public static SimpleDateResultWrapper of(Object result) {
        if (ClassUtils.isDate(result)) {
            if (result instanceof Date) {
                return new SimpleDateResultWrapper(((Date) result));
            } else if (result instanceof LocalDateTime) {
                return new SimpleDateResultWrapper(((LocalDateTime) result));
            }
        }
        return null;
    }

    @Override
    public LocalDateTime getResult() {
        return this.result;
    }

}
