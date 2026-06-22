package com.github.aqiu202.cron.time;

import java.time.Clock;
import java.time.ZonedDateTime;

public class NewDateTime extends AbstractDateTime<ZonedDateTime> {

    public NewDateTime(Clock clock) {
        this.value = ZonedDateTime.ofInstant(clock.instant(), clock.getZone());
    }
    public NewDateTime(ZonedDateTime zonedDateTime) {
        this.value = zonedDateTime;
    }

    private ZonedDateTime value;

    @Override
    public ZonedDateTime getTime() {
        return this.value;
    }

    @Override
    public void clearMilliSeconds() {
        this.value = this.value.withNano(0);
    }

    public int getSecond() {
        return this.value.getSecond();
    }

    public void setSecond(int second) {
        this.value = this.value.withSecond(second);
    }

    public int getMinute() {
        return this.value.getMinute();
    }

    public void setMinute(int minute) {
        this.value = this.value.withMinute(minute);
    }

    public int getHour() {
        return this.value.getHour();
    }

    public void setHour(int hour) {
        this.value = this.value.withHour(hour);
    }

    public int getDayOfMonth() {
        return this.value.getDayOfMonth();
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.value = this.value.withDayOfMonth(dayOfMonth);
    }

    public int getMonth() {
        return this.value.getMonthValue();
    }

    public void setMonth(int month) {
        this.value = this.value.withMonth(month);
    }

    public int getYear() {
        return this.value.getYear();
    }

    public void setYear(int year) {
        this.value = this.value.withYear(year);
    }

    @Override
    public int getDayOfWeek() {
        int v = this.value.getDayOfWeek().getValue() + 1;
        return v == 8 ? 1 : v;
    }

    @Override
    public void plusSeconds(int value) {
        this.value = this.value.plusSeconds(value);
    }

    @Override
    public void plusMinutes(int value) {
        this.value = this.value.plusMinutes(value);
    }

    @Override
    public void plusHours(int value) {
        this.value = this.value.plusHours(value);
    }

    @Override
    public void plusDays(int value) {
        this.value = this.value.plusDays(value);
    }

    @Override
    public void plusMonths(int value) {
        this.value = this.value.plusMonths(value);
    }

    @Override
    public void plusYears(int value) {
        this.value = this.value.plusYears(value);
    }
}
