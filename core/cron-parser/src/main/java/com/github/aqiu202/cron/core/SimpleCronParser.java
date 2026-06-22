package com.github.aqiu202.cron.core;

import com.github.aqiu202.cron.exp.InvalidCronException;
import com.github.aqiu202.cron.field.*;

import java.util.*;
import java.util.function.Function;

public class SimpleCronParser implements CronParser {


    protected static final Map<String, Integer> MONTH_MAP = new HashMap<>(20);
    protected static final Map<String, Integer> DAYS_OF_WEEK_MAP = new HashMap<>(16);

    static {
        MONTH_MAP.put("JAN", 1);
        MONTH_MAP.put("FEB", 2);
        MONTH_MAP.put("MAR", 3);
        MONTH_MAP.put("APR", 4);
        MONTH_MAP.put("MAY", 5);
        MONTH_MAP.put("JUN", 6);
        MONTH_MAP.put("JUL", 7);
        MONTH_MAP.put("AUG", 8);
        MONTH_MAP.put("SEP", 9);
        MONTH_MAP.put("OCT", 10);
        MONTH_MAP.put("NOV", 11);
        MONTH_MAP.put("DEC", 12);

        DAYS_OF_WEEK_MAP.put("SUN", 1);
        DAYS_OF_WEEK_MAP.put("MON", 2);
        DAYS_OF_WEEK_MAP.put("TUE", 3);
        DAYS_OF_WEEK_MAP.put("WED", 4);
        DAYS_OF_WEEK_MAP.put("THU", 5);
        DAYS_OF_WEEK_MAP.put("FRI", 6);
        DAYS_OF_WEEK_MAP.put("SAT", 7);
    }

    @Override
    public Cron parse(String expression) {
        SimpleCron sci = new SimpleCron();
        StringTokenizer st = new StringTokenizer(expression.toUpperCase(Locale.US), " \t");
        int index = CronConstants.INDEX_SECOND;
        int n_index = 0;
        while (st.hasMoreTokens() && index <= CronConstants.INDEX_YEAR) {
            String fieldToken = st.nextToken();
            CronField cronField = this.parseFieldToken(fieldToken, index);
            if (cronField instanceof UnknownCronField) {
                n_index++;
            }
            switch (index) {
                case CronConstants.INDEX_SECOND:
                    sci.setSeconds(((EnumerableCronField) cronField));
                    break;
                case CronConstants.INDEX_MINUTE:
                    sci.setMinutes(((EnumerableCronField) cronField));
                    break;
                case CronConstants.INDEX_HOUR:
                    sci.setHours(((EnumerableCronField) cronField));
                    break;
                case CronConstants.INDEX_DAYS_OF_MONTH:
                    sci.setDaysOfMonth(cronField);
                    break;
                case CronConstants.INDEX_MONTH:
                    sci.setMonths(((EnumerableCronField) cronField));
                    break;
                case CronConstants.INDEX_DAYS_OF_WEEK:
                    sci.setDaysOfWeek(cronField);
                    break;
                case CronConstants.INDEX_YEAR:
                    sci.setYears(((EnumerableCronField) cronField));
                    break;
            }
            index++;
        }
        if (n_index != 1) {
            throw new InvalidCronException(String.format("表达式格式异常-- %s用在DayOfMonth和DayOfWeek两个域，有且只能使用一次", CronConstants.VAL_UNKNOWN));
        }
        if (Objects.isNull(sci.getYears())) {
            sci.setYears(new EveryCronField(CronConstants.INDEX_YEAR));
        }
        return sci;
    }


    /**
     * 处理每个域的数据
     *
     * @param fieldToken 域字符
     * @param index      域索引
     * @return 解析结果
     */
    private CronField parseFieldToken(String fieldToken, int index) {
        if (CronConstants.VAL_EVERY.equals(fieldToken)) {
            return new EveryCronField(index);
        }
        if (CronConstants.VAL_UNKNOWN.equals(fieldToken)) {
            if (index == CronConstants.INDEX_DAYS_OF_MONTH || index == CronConstants.INDEX_DAYS_OF_WEEK) {
                return new UnknownCronField(index);
            } else {
                throw new InvalidCronException(String.format("表达式格式异常-- %s符号只能用在DayOfMonth和DayOfWeek两个域", CronConstants.VAL_UNKNOWN));
            }
        }
        // 单 token 直接解析，避免 SimpleCronFields + merge 的中间对象开销
        if (fieldToken.indexOf(CronConstants.SEPARATOR_ENUM) == -1) {
            return this.parseItem(fieldToken, index);
        }
        SimpleCronFields result = new SimpleCronFields(index);
        StringTokenizer ist = new StringTokenizer(fieldToken, String.valueOf(CronConstants.SEPARATOR_ENUM));
        while (ist.hasMoreTokens()) {
            CronField cf = this.parseItem(ist.nextToken(), index);
            if (cf instanceof EnumerableCronField) {
                result.add(((EnumerableCronField) cf));
            } else {
                return cf;
            }
        }
        return result.merge();
    }

    /**
     * 处理每个域可枚举的单个数据
     *
     * @param token 数据
     * @param index 域索引
     * @return 解析结果
     */
    private CronField parseItem(String token, int index) {
        int i;
        if (token.endsWith(CronConstants.VAL_WEEKDAY)) {
            if (index != CronConstants.INDEX_DAYS_OF_MONTH) {
                throw new InvalidCronException(String.format("表达式格式异常-- %s 只能用在DaysOfMonth域",
                        CronConstants.SEPARATOR_WEEKDAY));
            }
            if (CronConstants.VAL_LAST_WEEKDAY.equals(token)) {
                return new LastWeekdayCronField(index);
            }
            if (CronConstants.VAL_WEEKDAY.equals(token)) {
                throw new InvalidCronException(String.format("表达式异常-- %s 使用时必须与数字或者%s组合使用",
                        CronConstants.SEPARATOR_WEEKDAY, CronConstants.SEPARATOR_LAST));
            }
            return new WeekdayCronField(index, this.strToInt(token.substring(0, token.length() - 1)));
        } else if ((i = token.indexOf(CronConstants.SEPARATOR_LAST)) > -1) {
            // L仅支持在DaysOfMonth和DaysOfWeek域使用
            if (index != CronConstants.INDEX_DAYS_OF_MONTH && index != CronConstants.INDEX_DAYS_OF_WEEK) {
                throw new InvalidCronException(String.format("表达式格式异常-- %s 只能用在DaysOfMonth和DaysOfWeek域",
                        CronConstants.SEPARATOR_LAST));
            }
            // 如果在DaysOfMonth域 L代表最后一天
            if (index == CronConstants.INDEX_DAYS_OF_MONTH) {
                if (CronConstants.VAL_LAST.equals(token)) {
                    return new LastCronField(index, 0);
                }
                throw new InvalidCronException(String.format("表达式格式异常-- %s 用法错误", CronConstants.SEPARATOR_LAST));
            } else {
                // 如果在DaysOfWeek域 L代表最后一个星期日（即1L）
                return new LastCronField(index, i == 0 ? 0 : this.strToInt(token.substring(0, i)));
            }
        } else if ((i = token.indexOf(CronConstants.SEPARATOR_INDEX)) > -1) {
            // #仅支持在DaysOfWeek域使用
            if (index != CronConstants.INDEX_DAYS_OF_WEEK) {
                throw new InvalidCronException(String.format("表达式格式异常-- 符号 %s 只能用在DaysOfWeek域", CronConstants.SEPARATOR_INDEX));
            }
            int val1 = this.strToInt(token.substring(0, i));
            int val2 = this.strToInt(token.substring(i + 1));
            return new IndexCronField(index, val1, val2);
        } else if ((i = token.indexOf(CronConstants.SEPARATOR_RANGE)) > -1) {
            String val1 = token.substring(0, i);
            String val2 = token.substring(i + 1);
            int start = CronConstants.VAL_EVERY.equals(val1) ? this.handleEvery(index) : this.parseTokenInt(val1, index);
            int end = this.parseTokenInt(val2, index);
            return new RangeCronField(index, start, end);
        } else if ((i = token.indexOf(CronConstants.SEPARATOR_STEP)) > -1) {
            String val1 = token.substring(0, i);
            String val2 = token.substring(i + 1);
            int start = CronConstants.VAL_EVERY.equals(val1) ? this.handleEvery(index) : this.parseTokenInt(val1, index);
            int end = this.parseTokenInt(val2, index);
            return new StepCronField(index, start, end);
        } else {
            return new EnumerableCronField(index, this.parseSimpleValues(token, index));
        }
    }

    private int parseTokenInt(String token, int index) {
        if (index == CronConstants.INDEX_MONTH) {
            return this.parseMonth(token);
        } else if (index == CronConstants.INDEX_DAYS_OF_WEEK) {
            return this.parseDaysOfWeek(token);
        } else {
            return this.strToInt(token);
        }
    }

    private List<Integer> parseSimpleValues(String token, int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
            case CronConstants.INDEX_MINUTE:
            case CronConstants.INDEX_HOUR:
            case CronConstants.INDEX_YEAR:
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return Collections.singletonList(this.strToInt(token));
            case CronConstants.INDEX_MONTH:
                return Collections.singletonList(this.parseMonth(token));
            case CronConstants.INDEX_DAYS_OF_WEEK:
                return Collections.singletonList(this.parseDaysOfWeek(token));
        }
        return null;
    }

    private Integer parseMonth(String month) {
        Integer value;
        month = this.supportC(month);
        if (Character.isDigit(month.charAt(0))) {
            value = this.strToInt(month);
        } else {
            value = MONTH_MAP.get(month);
        }
        if (value == null) {
            throw new InvalidCronException("月份数据格式异常");
        }
        return value;
    }

    private Integer strToInt(String val) {
        try {
            return Integer.valueOf(val);
        } catch (NumberFormatException e) {
            throw new InvalidCronException(String.format("表达式格式异常-- 异常的数字格式-%s", val));
        }
    }

    private Integer parseDaysOfWeek(String daysOfWeek) {
        Integer value;
        daysOfWeek = this.supportC(daysOfWeek);
        if (Character.isDigit(daysOfWeek.charAt(0))) {
            value = this.strToInt(daysOfWeek);
        } else {
            value = DAYS_OF_WEEK_MAP.get(daysOfWeek);
        }
        if (value == null) {
            throw new InvalidCronException("星期数据格式异常");
        }
        return value;
    }

    private String supportC(String token) {
        return token.replace("C", "");
    }

    private int handleEvery(int index) {
        switch (index) {
            case CronConstants.INDEX_SECOND:
                return CronConstants.MIN_SECOND;
            case CronConstants.INDEX_MINUTE:
                return CronConstants.MIN_MINUTE;
            case CronConstants.INDEX_HOUR:
                return CronConstants.MIN_HOUR;
            case CronConstants.INDEX_DAYS_OF_MONTH:
                return CronConstants.MIN_DAYS_OF_MONTH;
            case CronConstants.INDEX_MONTH:
                return CronConstants.MIN_MONTH;
            case CronConstants.INDEX_DAYS_OF_WEEK:
                return CronConstants.MIN_DAYS_OF_WEEK;
            case CronConstants.INDEX_YEAR:
                return CronConstants.MIN_YEAR;
        }
        return 0;
    }

}
