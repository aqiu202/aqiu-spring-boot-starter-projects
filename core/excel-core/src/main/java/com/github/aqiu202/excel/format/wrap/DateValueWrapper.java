package com.github.aqiu202.excel.format.wrap;

import com.github.aqiu202.util.ClassUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateValueWrapper implements ValueWrapper<LocalDateTime> {

    private final LocalDateTime result;

    public DateValueWrapper(LocalDateTime result) {
        this.result = result;
    }

    public DateValueWrapper(Date result) {
        this(LocalDateTime.ofInstant((result).toInstant(), ZoneId.systemDefault()));
    }

    public static DateValueWrapper of(Object result) {
        if (ClassUtils.isDate(result)) {
            if (result instanceof Date) {
                return new DateValueWrapper(((Date) result));
            } else if (result instanceof LocalDateTime) {
                return new DateValueWrapper(((LocalDateTime) result));
            }
        }
        return null;
    }

    @Override
    public LocalDateTime getValue() {
        return this.result;
    }

}
