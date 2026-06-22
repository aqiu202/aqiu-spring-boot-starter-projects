package com.github.aqiu202.cron.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleDateTime extends AbstractDateTime<Date> {
    public SimpleDateTime(Date date) {
        this.calendar = new GregorianCalendar();
        this.calendar.setTime(date);
    }

    private final Calendar calendar;

    @Override
    public Date getTime() {
        return this.calendar.getTime();
    }

    @Override
    public void clearMilliSeconds() {
        this.calendar.set(Calendar.MILLISECOND, 0);
    }

    public int getSecond() {
        return this.calendar.get(Calendar.SECOND);
    }

    public void setSecond(int second) {
        this.calendar.set(Calendar.SECOND, second);
    }

    public int getMinute() {
        return this.calendar.get(Calendar.MINUTE);
    }

    public void setMinute(int minute) {
        this.calendar.set(Calendar.MINUTE, minute);
    }

    public int getHour() {
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    public void setHour(int hour) {
        this.calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    public int getDayOfMonth() {
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public int getMonth() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    public void setMonth(int month) {
        this.calendar.set(Calendar.MONTH, month - 1);
    }

    public int getYear() {
        return this.calendar.get(Calendar.YEAR);
    }

    public void setYear(int year) {
        this.calendar.set(Calendar.YEAR, year);
    }

    @Override
    public int getDayOfWeek() {
        return this.calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public void plusSeconds(int value) {
        this.calendar.add(Calendar.SECOND, value);
    }

    @Override
    public void plusMinutes(int value) {
        this.calendar.add(Calendar.MINUTE, value);
    }

    @Override
    public void plusHours(int value) {
        this.calendar.add(Calendar.HOUR_OF_DAY, value);
    }

    @Override
    public void plusDays(int value) {
        this.calendar.add(Calendar.DAY_OF_MONTH, value);
    }

    @Override
    public void plusMonths(int value) {
        this.calendar.add(Calendar.MONTH, value);
    }

    @Override
    public void plusYears(int value) {
        this.calendar.add(Calendar.YEAR, value);
    }
}
