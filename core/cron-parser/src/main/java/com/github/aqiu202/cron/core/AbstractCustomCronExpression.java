package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.exp.NoValidValueException;
import com.github.aqiu202.cron.field.*;
import com.github.aqiu202.cron.time.DateTime;
import com.github.aqiu202.cron.util.CronUtils;

public abstract class AbstractCustomCronExpression<S, T> extends AbstractCronExpression<S, T> {

    private static final CronParser PARSER = new SimpleCronParser();

    protected AbstractCustomCronExpression(String expression) {
        super(expression);
        this.cron = PARSER.parse(expression);
    }

    protected final Cron cron;

    public Cron getCron() {
        return cron;
    }

    protected void process(DateTime<?> dateTime) {
        int dayOfMonth0 = dateTime.getDayOfMonth();
        int month0 = dateTime.getMonth();
        int year0 = dateTime.getYear();
        Cron cron = this.cron;
        this.resolveSMH(dateTime);
        CronField daysOfMonth = cron.getDaysOfMonth();
        if (daysOfMonth instanceof UnknownCronField) {
            this.findMatchedDayOfWeekTime(cron, dateTime);
        } else {
            this.findMatchedDayOfMonthTime(cron, dateTime);
        }
        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonth();
        int year = dateTime.getYear();
        if (dayOfMonth0 != day || month0 != month || year0 != year) {
            dateTime.resetLeft(CronConstants.INDEX_DAYS_OF_MONTH, cron);
        }
    }

    /**
     * 解析秒分时字段
     *
     * @param dateTime dateTime
     */
    protected void resolveSMH(DateTime<?> dateTime) {
        Cron cron = this.cron;
        this.resolveEnumerableField(cron.getSeconds(), dateTime);
        this.resolveEnumerableField(cron.getMinutes(), dateTime);
        this.resolveEnumerableField(cron.getHours(), dateTime);
    }

    protected void resolveEnumerableField(EnumerableCronField field, DateTime<?> dateTime) {
        int index = field.getIndex();
        int value0 = dateTime.get(index);
        ValueResult vw = field.findAfter(value0, dateTime);
        if (vw.isOutOfBounds()) {
            this.resolveOutOfBounds(index, dateTime);
        }
        Integer value = vw.getValue();
        dateTime.set(index, value);
        if (index > 0 && value != value0) {
            dateTime.resetLeft(index, cron);
        }
    }

    protected void resolveOutOfBounds(int index, DateTime<?> dateTime) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                dateTime.plusMinutes(1);
                break;
            case CronConstants.INDEX_MINUTE:
                dateTime.plusHours(1);
                break;
            case CronConstants.INDEX_HOUR:
                dateTime.plusDays(1);
                break;
            case CronConstants.INDEX_DAYS_OF_MONTH:
                dateTime.plusMonths(1);
                break;
            case CronConstants.INDEX_MONTH:
                if (dateTime.getYear() >= CronConstants.MAX_YEAR) {
                    // 年份超出最大值
                    throw new NoValidValueException("不存在匹配的时间：超出最大时间范围");
                }
                dateTime.plusYears(1);
                break;
        }
    }

    private void findMatchedDayOfMonthTime(Cron cron, DateTime<?> dateTime) {
        EnumerableCronField months = cron.getMonths();
        CronField daysOfMonth = cron.getDaysOfMonth();
        if (daysOfMonth instanceof EnumerableCronField) {
            ValueResult vw = ((EnumerableCronField) daysOfMonth).findAfter(dateTime.getDayOfMonth(), dateTime);
            if (!vw.isValid() || vw.isOutOfBounds()) {
                this.findMatchedDayOfMonthTimeWithNextMonth(cron, dateTime);
                return;
            }
            dateTime.setDayOfMonth(vw.getValue());
            int month = dateTime.getMonth();
            ValueResult mvw = months.findAfter(month, dateTime);
            Integer monthVal = mvw.getValue();
            if (!mvw.isValid()) {
                this.setMonth(monthVal, dateTime);
                this.findMatchedDayOfMonthTimeWithNextMonth(cron, dateTime);
                return;
            } else if (mvw.isOutOfBounds()) {
                this.findMatchedDayOfMonthTimeWithNextYear(cron, dateTime);
                return;
            }
            dateTime.setMonth(monthVal);
            if (month != monthVal) {
                dateTime.resetLeft(CronConstants.INDEX_MONTH, cron);
            }
            ValueResult yvw = cron.getYears().findAfter(dateTime.getYear(), dateTime);
            Integer yearVal = yvw.getValue();
            if (!yvw.isValid()) {
                this.findMatchedDayOfMonthTimeWithNextYear(yearVal, cron, dateTime);
                return;
            }
            dateTime.setYear(yearVal);
            if (yearVal != dateTime.getYear()) {
                dateTime.resetLeft(CronConstants.INDEX_YEAR, cron);
            }
        } else if (daysOfMonth instanceof LastWeekdayCronField) {
            int year0 = dateTime.getYear();
            int month0 = dateTime.getMonth();
            this.processYearAndMonth(cron, dateTime);
            int month2 = dateTime.getMonth();
            int year2 = dateTime.getYear();
            int maxDays = CronUtils.getLastDayOfMonth(month2, year2);
            int day0 = dateTime.getDayOfMonth();
            dateTime.setDayOfMonth(maxDays);
            int dayOfWeek = dateTime.getDayOfWeek();
            if (!CronUtils.isWeekday(dayOfWeek)) {
                int offset = dayOfWeek % 7 + 1;
                maxDays -= offset;
                if (month0 == month2 && year0 == year2 && day0 > maxDays) {
                    this.findMatchedDayOfMonthTimeWithNextMonth(cron, dateTime);
                    return;
                }
                dateTime.setDayOfMonth(maxDays);
            }
        } else if (daysOfMonth instanceof LastCronField) {
            int maxDays = CronUtils.getLastDayOfMonth(dateTime.getMonth(), dateTime.getYear());
            dateTime.setDayOfMonth(maxDays);
        } else if (daysOfMonth instanceof WeekdayCronField) {
            this.processYearAndMonth(cron, dateTime);
            int value = ((WeekdayCronField) daysOfMonth).getValue();
            int month = dateTime.getMonth();
            int year = dateTime.getYear();
            int maxDays = CronUtils.getLastDayOfMonth(month, year);
            if (value > maxDays) {
                this.findMatchedDayOfMonthTimeWithNextMonth(cron, dateTime);
                return;
            }
            int day0 = dateTime.getDayOfMonth();
            dateTime.setDayOfMonth(value);
            int dayOfWeek = dateTime.getDayOfWeek();
            if (dayOfWeek == 7) {
                value--;
            }
            if (dayOfWeek == 1) {
                //如果当前日期是当月最后一天，不跨月，向前查找
                if (value == maxDays) {
                    value -= 2;
                } else {
                    value++;
                }
            }
            if (day0 > value) {
                this.findMatchedDayOfMonthTimeWithNextMonth(cron, dateTime);
                return;
            }
            dateTime.setDayOfMonth(value);
        }
    }

    private void processYearAndMonth(Cron cron, DateTime<?> dateTime) {
        EnumerableCronField years = cron.getYears();
        EnumerableCronField months = cron.getMonths();
        int year0 = dateTime.getYear();
        int month0 = dateTime.getMonth();
        ValueResult yvw = years.findAfter(year0, dateTime);
        int year2 = yvw.getValue();
        if (year2 != year0) {
            this.setYear(year2, cron, dateTime);
        }
        ValueResult mvw = months.findAfter(month0, dateTime);
        int month2 = mvw.getValue();
        if (mvw.isOutOfBounds()) {
            if (year0 >= CronConstants.MAX_YEAR) {
                // 年份超出最大值
                throw new NoValidValueException("不存在匹配的时间");
            }
            this.setYear(years.findAfter(year0 + 1, dateTime).getValue(), cron, dateTime);
        }
        if (month0 != month2) {
            this.setMonth(month2, dateTime);
        }
    }

    private void findMatchedDayOfWeekTime(Cron cron, DateTime<?> dateTime) {
        this.processYearAndMonth(cron, dateTime);
        int year = dateTime.getYear();
        int month = dateTime.getMonth();
        CronField daysOfWeek = cron.getDaysOfWeek();
        if (daysOfWeek instanceof IndexCronField) {
            int day0 = dateTime.getDayOfMonth();
            this.resetDayOfMonth(dateTime);
            IndexCronField dowIndexVal = (IndexCronField) daysOfWeek;
            int value = dowIndexVal.getValue();
            int count = dowIndexVal.getCount();
            int offset = (count - 1) * 7;
            int dow2 = dateTime.getDayOfWeek();
            int diff = value - dow2;
            if (diff < 0) {
                diff += 7;
            }
            offset += diff;
            int days = offset + 1;
            if (day0 > days) {
                this.findMatchedDayOfWeekTimeWithNextMonth(cron, dateTime);
                return;
            }
            caseDaysCross(cron, dateTime, year, month, days);
        } else if (daysOfWeek instanceof EnumerableCronField) {
            int dom = dateTime.getDayOfMonth();
            int dow = dateTime.getDayOfWeek();
            ValueResult vw = ((EnumerableCronField) daysOfWeek).findAfter(dow, dateTime);
            int offset = vw.getValue() - dow;
            if (vw.isOutOfBounds()) {
                offset += 7;
            }
            int days = dom + offset;
            caseDaysCross(cron, dateTime, year, month, days);
        } else if (daysOfWeek instanceof LastCronField) {
            int value = ((LastCronField) daysOfWeek).getValue();
            int maxDays = CronUtils.getLastDayOfMonth(month, year);
            int day0 = dateTime.getDayOfMonth();
            int day;
            boolean monthNotMatched;
            if (value > 0) {
                dateTime.setDayOfMonth(maxDays);
                int dayOfWeek = dateTime.getDayOfWeek();
                int offset = dayOfWeek - value;
                if (offset < 0) {
                    offset += 7;
                }
                day = maxDays - offset;
                // 如果日期在当前日期之前，则加一月继续查找
                monthNotMatched = day0 > day;
            } else {
                int dayOfWeek = dateTime.getDayOfWeek();
                int offset = 7 - dayOfWeek;
                day = day0 + offset;
                // 如果跨月了，加一月继续查找
                monthNotMatched = day > maxDays;
            }
            if (monthNotMatched) {
                this.findMatchedDayOfWeekTimeWithNextMonth(cron, dateTime);
                return;
            }
            dateTime.setDayOfMonth(day);
        }
    }

    private void caseDaysCross(Cron ci, DateTime<?> dateTime, int year, int month, int days) {
        int maxDays = CronUtils.getLastDayOfMonth(month, year);
        if (days > maxDays) {
            this.whenMonthCross(ci, dateTime, year);
        } else {
            dateTime.setDayOfMonth(days);
        }
    }

    private void whenMonthCross(Cron ci, DateTime<?> dateTime, int year) {
        dateTime.plusMonths(1);
        if (dateTime.getYear() != year) {
            this.resetMonth(ci, dateTime);
        } else {
            this.resetDayOfMonth(dateTime);
        }
        this.findMatchedDayOfWeekTime(ci, dateTime);
    }

    private void setYear(int value, Cron ci, DateTime<?> dateTime) {
        dateTime.setYear(value);
        this.resetMonth(ci, dateTime);
    }

    private void resetMonth(Cron ci, DateTime<?> dateTime) {
        this.setMonth(ci.getMonths().firstValue(), dateTime);
    }

    private void setMonth(int value, DateTime<?> dateTime) {
        this.resetDayOfMonth(dateTime);
        dateTime.setMonth(value);
    }

    private void resetDayOfMonth(DateTime<?> dateTime) {
        dateTime.setDayOfMonth(1);
    }

    private void findMatchedDayOfWeekTimeWithNextMonth(Cron cron, DateTime<?> dateTime) {
        this.resetDayOfMonth(dateTime);
        dateTime.plusMonths(1);
        this.findMatchedDayOfWeekTime(cron, dateTime);
    }

    private void findMatchedDayOfMonthTimeWithNextMonth(Cron cron, DateTime<?> dateTime) {
        this.resetDayOfMonth(dateTime);
        dateTime.plusMonths(1);
        this.findMatchedDayOfMonthTime(cron, dateTime);
    }

    private void findMatchedDayOfMonthTimeWithNextYear(Cron cron, DateTime<?> dateTime) {
        this.setMonth(1, dateTime);
        dateTime.plusYears(1);
        this.findMatchedDayOfMonthTime(cron, dateTime);
    }

    private void findMatchedDayOfMonthTimeWithNextYear(int year, Cron cron, DateTime<?> dateTime) {
        this.setMonth(1, dateTime);
        dateTime.setYear(year);
        this.findMatchedDayOfMonthTime(cron, dateTime);
    }

    public T nextExecution(S s) {
        DateTime<T> dateTime = this.createDateTimeInstance(s);
        return this.processTime(dateTime);
    }

    protected T processTime(DateTime<T> time) {
        time.init();
        this.process(time);
        return time.getTime();
    }

    protected abstract DateTime<T> createDateTimeInstance(S s);

}
