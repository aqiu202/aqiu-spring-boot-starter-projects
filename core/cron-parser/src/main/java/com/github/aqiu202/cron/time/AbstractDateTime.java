package com.github.aqiu202.cron.time;

public abstract class AbstractDateTime<T> implements DateTime<T> {

    private DateTimeDefinition origin;

    @Override
    public void init() {
        this.ceilToSeconds();
        this.copyToDefinition();
    }

    protected void copyToDefinition() {
        SimpleDateTimeDefinition dateTimeDefinition = new SimpleDateTimeDefinition();
        dateTimeDefinition.setSecond(this.getSecond());
        dateTimeDefinition.setMinute(this.getMinute());
        dateTimeDefinition.setHour(this.getHour());
        dateTimeDefinition.setDayOfMonth(this.getDayOfMonth());
        dateTimeDefinition.setMonth(this.getMonth());
        dateTimeDefinition.setYear(this.getYear());
        this.origin = dateTimeDefinition;
    }

    @Override
    public DateTimeDefinition getOrigin() {
        return origin;
    }

    @Override
    public void ceilToSeconds() {
        this.plusSeconds(1);
        this.clearMilliSeconds();
    }

    protected abstract void clearMilliSeconds();

    @Override
    public String toString() {
        return this.getYear() + "-" + this.getMonth() + "-" + this.getDayOfMonth() + " "
                + this.getHour() + ":" + this.getMinute() + ":" + this.getSecond();
    }
}
