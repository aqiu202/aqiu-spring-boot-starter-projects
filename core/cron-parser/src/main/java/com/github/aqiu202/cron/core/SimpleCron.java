package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.field.CronField;
import com.github.aqiu202.cron.field.EnumerableCronField;

public class SimpleCron implements Cron {

    private EnumerableCronField seconds;

    private EnumerableCronField minutes;

    private EnumerableCronField hours;

    private CronField daysOfMonth;

    private EnumerableCronField months;

    private CronField daysOfWeek;

    private EnumerableCronField years;

    @Override
    public EnumerableCronField getSeconds() {
        return seconds;
    }

    public void setSeconds(EnumerableCronField seconds) {
        this.seconds = seconds;
    }

    @Override
    public EnumerableCronField getMinutes() {
        return minutes;
    }

    public void setMinutes(EnumerableCronField minutes) {
        this.minutes = minutes;
    }

    @Override
    public EnumerableCronField getHours() {
        return hours;
    }

    public void setHours(EnumerableCronField hours) {
        this.hours = hours;
    }

    @Override
    public CronField getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(CronField daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    @Override
    public EnumerableCronField getMonths() {
        return months;
    }

    public void setMonths(EnumerableCronField months) {
        this.months = months;
    }

    @Override
    public CronField getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(CronField daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public EnumerableCronField getYears() {
        return years;
    }

    public void setYears(EnumerableCronField years) {
        this.years = years;
    }

}
