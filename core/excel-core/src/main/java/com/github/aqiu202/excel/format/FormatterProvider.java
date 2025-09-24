package com.github.aqiu202.excel.format;

public interface FormatterProvider {

    String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";

    String DEFAULT_NUM_FORMAT_PATTERN = "#.##";

    Class<? extends DateFormatter> DEFAULT_DATE_FORMATTER = SimpleDateFormatter.class;
    Class<? extends NumberFormatter> DEFAULT_NUM_FORMATTER = DecimalFormatter.class;
    Class<? extends NullFormatter> DEFAULT_NULL_FORMATTER = SimpleNullFormatter.class;

    boolean isEnableDefaultFormatter();

    Class<? extends NumberFormatter> getNumberFormatter();

    String getNumberFormat();

    Class<? extends DateFormatter> getDateFormatter();

    String getDateFormat();

    Class<? extends NullFormatter> getNullFormatter();
}
