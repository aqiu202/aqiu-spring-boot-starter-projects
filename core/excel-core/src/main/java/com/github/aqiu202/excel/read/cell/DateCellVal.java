package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateCellVal extends SimpleCellVal<LocalDateTime> {

    public DateCellVal(Cell cell, LocalDateTime value) {
        super(cell, value);
    }

    public static DateCellVal of(Cell cell, Object value) {
        if (value instanceof LocalDate) {
            return new DateCellVal(cell, LocalDateTime.of(((LocalDate) value), LocalTime.MIN));
        } else if (value instanceof LocalTime) {
            return new DateCellVal(cell, LocalDateTime.of(LocalDate.MIN, ((LocalTime) value)));
        } else if (value instanceof LocalDateTime) {
            return new DateCellVal(cell, ((LocalDateTime) value));
        } else if (value instanceof Date) {
            return new DateCellVal(cell, LocalDateTime.ofInstant(((Date)value).toInstant(), ZoneId.systemDefault()));
        }
        return new DateCellVal(cell, null);
    }

    public Date getDate() {
        return Date.from(this.getValue().atZone(ZoneId.systemDefault()).toInstant());
    }

    public <T> T convertAs(Class<T> type) {
        if (Date.class.isAssignableFrom(type)) {
            return (T) this.getDate();
        }
        if (type.equals(LocalDateTime.class)) {
            return (T) this.getValue();
        }
        if (type.equals(LocalDate.class)) {
            return (T) this.getValue().toLocalDate();
        }
        if (type.equals(LocalTime.class)) {
            return (T) this.getValue().toLocalTime();
        }
        return null;
    }
}
