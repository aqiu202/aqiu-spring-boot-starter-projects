package com.github.aqiu202.excel.format.wrap;

import com.github.aqiu202.util.ClassUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateValueWrapper implements ValueWrapper<Date> {

    private final Date value;

    public DateValueWrapper(Date value) {
        this.value = value;
    }

    public DateValueWrapper(LocalDateTime value) {
        // 将Instant转换为Date
        this.value = Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static DateValueWrapper of(Object result) {
        if (ClassUtils.isDate(result)) {
            if (result instanceof Date) {
                return new DateValueWrapper(((Date) result));
            } else if (result instanceof LocalDateTime) {
                return new DateValueWrapper(((LocalDateTime) result));
            } else if (result instanceof LocalDate) {
                return new DateValueWrapper(LocalDateTime.of((LocalDate) result, LocalTime.MIN));
            } else if (result instanceof LocalTime) {
                return new DateValueWrapper(LocalDateTime.of(LocalDate.MIN, (LocalTime) result));
            }
        }
        return null;
    }

    @Override
    public Date getValue() {
        return this.value;
    }

}
