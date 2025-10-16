package com.github.aqiu202.starters.jpa.type;

import com.github.aqiu202.util.wrap.ObjectWrapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/25 16:43
 **/
public final class DefaultTypeConverter implements TypeConverter {

    private DefaultTypeConverter() {
    }

    //静态内部类实现单例安全懒加载
    private static class InnerClass {

        private static final TypeConverter java8DateTypeConverter = new Java8DateTypeConverter();
        private static final TypeConverter numberTypeConverter = new NumberTypeConverter();
        private static final TypeConverter defaultTypeConverter = new DefaultTypeConverter();
    }

    public static TypeConverter getJava8DateTypeConverter() {
        return InnerClass.java8DateTypeConverter;
    }

    public static TypeConverter getNumberTypeConverter() {
        return InnerClass.numberTypeConverter;
    }

    public static TypeConverter getDefaultTypeConverter() {
        return InnerClass.defaultTypeConverter;
    }

    private Collection<TypeConverter> getTypeConverters() {
        return Arrays.asList(getJava8DateTypeConverter(), getNumberTypeConverter());
    }

    @Override
    public void convert(ObjectWrapper wrapper, Class<?> type) {
        this.getTypeConverters().forEach(c -> c.convert(wrapper, type));
    }

    static class NumberTypeConverter implements TypeConverter {

        @Override
        public void convert(ObjectWrapper wrapper, Class<?> type) {
            Object value = wrapper.get();
            if (Number.class.isAssignableFrom(type) || type.isPrimitive() || String.class.isAssignableFrom(type)) {
                if(value instanceof Number) {
                    Number n = (Number) value;
                    if (type.equals(Long.class) || type.equals(long.class)) {
                        value = n.longValue();
                    } else if (type.equals(Integer.class) || type.equals(int.class)) {
                        value = n.intValue();
                    } else if (type.equals(Short.class) || type.equals(short.class)) {
                        value = n.shortValue();
                    } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                        value = n.byteValue();
                    } else if (type.equals(Float.class) || type.equals(float.class)) {
                        value = n.floatValue();
                    } else if (type.equals(Double.class) || type.equals(double.class)) {
                        value = n.doubleValue();
                    } else if (type.equals(BigInteger.class)) {
                        value = BigInteger.valueOf(n.longValue());
                    } else if (type.equals(BigDecimal.class)) {
                        value = BigDecimal.valueOf(n.doubleValue());
                    } else if(type.equals(String.class)) {
                        //目标类型为字符类型
                        value = n.toString();
                    }
                } else if(value instanceof String) {
                    String s = (String) value;
                    if (type.equals(Long.class) || type.equals(long.class)) {
                        value = new Long(s);
                    } else if (type.equals(Integer.class) || type.equals(int.class)) {
                        value = new Integer(s);
                    } else if (type.equals(Short.class) || type.equals(short.class)) {
                        value = new Short(s);
                    } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                        value = new Byte(s);
                    } else if (type.equals(Float.class) || type.equals(float.class)) {
                        value = new Float(s);
                    } else if (type.equals(Double.class) || type.equals(double.class)) {
                        value = new Double(s);
                    } else if (type.equals(BigInteger.class)) {
                        value = new BigInteger(s);;
                    } else if (type.equals(BigDecimal.class)) {
                        value = new BigDecimal(s);;
                    }
                }
                wrapper.set(value);
            }
        }
    }

    static class Java8DateTypeConverter implements TypeConverter {

        private static final List<Class<?>> java8DateTypes = Arrays.asList(LocalDateTime.class,
            LocalDate.class, LocalTime.class);

        @Override
        public void convert(ObjectWrapper wrapper, Class<?> type) {
            if (java8DateTypes.contains(type)) {
                Object value = wrapper.get();
                if (value instanceof Timestamp && type.equals(LocalDateTime.class)) {
                    value = ((Timestamp) value).toLocalDateTime();
                } else if (value instanceof Date && type.equals(LocalDate.class)) {
                    value = ((Date) value).toLocalDate();
                } else if (value instanceof Time && type.equals(LocalTime.class)) {
                    value = ((Time) value).toLocalTime();
                }
                wrapper.set(value);
            }
        }
    }
}
