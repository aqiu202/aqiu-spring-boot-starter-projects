package com.github.aqiu202.starters.jpa.page;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.Assert;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/12/1 15:47
 **/
public class JPASort extends QSort {

    private static final Map<String, ComparableExpression<? extends Comparable<?>>> fieldsCache = new HashMap<>();

    public JPASort(EntityPathBase<?> queryEntity, com.querydsl.core.types.Order order,
                   String... properties) {
        super(toOrderSpecifiers(queryEntity, order, properties));
    }

    public JPASort(EntityPathBase<?> queryEntity, List<com.querydsl.core.types.Order> orders,
                   String... properties) {
        super(toOrderSpecifiers(queryEntity, orders, Arrays.asList(properties)));
    }

    public JPASort(OrderSpecifier<?>... orderSpecifiers) {
        super(orderSpecifiers);
    }

    private static List<OrderSpecifier<?>> toOrderSpecifiers(EntityPathBase<?> queryEntity,
                                                             com.querydsl.core.types.Order order, String... properties) {
        final Class<? extends EntityPathBase> clazz = queryEntity.getClass();
        final String name = clazz.getName();
        List<OrderSpecifier<?>> list = new ArrayList<>();
        for (String property : properties) {
            String key = name + ":" + property;
            ComparableExpression<? extends Comparable<?>> value;
            if ((value = fieldsCache.get(key)) == null) {
                try {
                    final Field field = clazz.getField(property);
                    value = (ComparableExpression<? extends Comparable<?>>) field.get(queryEntity);
                    synchronized (fieldsCache) {
                        fieldsCache.put(key, value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new IllegalArgumentException("排序字段不存在：" + property);
                }
            }
            list.add(new OrderSpecifier<>(order, value));
        }
        return list;
    }

    private static List<OrderSpecifier<?>> toOrderSpecifiers(EntityPathBase<?> queryEntity,
                                                             List<com.querydsl.core.types.Order> orders, List<String> properties) {
        Assert.isTrue(orders.size() == properties.size(), "排序参数异常");
        final Class<? extends EntityPathBase> clazz = queryEntity.getClass();
        final String name = clazz.getName();
        List<OrderSpecifier<?>> list = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            String property = properties.get(i);
            String key = name + ":" + property;
            ComparableExpression<? extends Comparable<?>> value;
            if ((value = fieldsCache.get(key)) == null) {
                try {
                    final Field field = clazz.getField(property);
                    value = (ComparableExpression<? extends Comparable<?>>) field.get(queryEntity);
                    synchronized (fieldsCache) {
                        fieldsCache.put(key, value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new IllegalArgumentException("排序字段不存在：" + property);
                }
            }
            list.add(new OrderSpecifier<>(orders.get(i), value));
        }
        return list;
    }

}
