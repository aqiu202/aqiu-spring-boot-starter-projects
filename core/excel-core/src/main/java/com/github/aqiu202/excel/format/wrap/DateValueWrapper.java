package com.github.aqiu202.excel.format.wrap;

import com.github.aqiu202.util.ClassUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateValueWrapper implements ValueWrapper<Date> {

    private final Date result;

    public DateValueWrapper(Date result) {
        this.result = result;
    }

    public DateValueWrapper(LocalDateTime result) {
        // 将Instant转换为Date
        this(Date.from(result.atZone(ZoneId.systemDefault()).toInstant()));
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
    public Date getValue() {
        return this.result;
    }

}
