package com.github.aqiu202.starters.jpa.page;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class DslSort implements Iterable<OrderSpecifier> {

    private static final DslSort UNSORTED = DslSort.by();

    private List<OrderSpecifier> orderSpecifiers;

    private DslSort(OrderSpecifier... orderSpecifiers) {
        this.orderSpecifiers = new ArrayList<>(Arrays.asList(orderSpecifiers));
    }

    private DslSort(List<OrderSpecifier> orderSpecifiers) {
        this.orderSpecifiers = orderSpecifiers;
    }

    public static DslSort by(OrderSpecifier... orderSpecifiers) {
        return new DslSort(orderSpecifiers);
    }

    public static DslSort by(List<OrderSpecifier> orderSpecifiers) {
        return new DslSort(orderSpecifiers);
    }

    public static DslSort asc(List<Expression> expressions) {
        return new DslSort(
                expressions.stream().map(e -> new OrderSpecifier(Order.ASC, e)).collect(
                        Collectors.toList()));
    }

    public static DslSort asc(Expression... expressions) {
        List<OrderSpecifier> orderSpecifiers = Stream.of(expressions)
                .map(e -> new OrderSpecifier(Order.ASC, e)).collect(
                        Collectors.toList());
        return new DslSort(orderSpecifiers);
    }

    public static DslSort desc(List<Expression> expressions) {
        return new DslSort(
                expressions.stream().map(e -> new OrderSpecifier(Order.DESC, e)).collect(
                        Collectors.toList()));
    }

    public static DslSort desc(Expression... expressions) {
        return new DslSort(
                Stream.of(expressions).map(e -> new OrderSpecifier(Order.DESC, e)).collect(
                        Collectors.toList()));
    }

    List<OrderSpecifier> getOrderSpecifiers() {
        return orderSpecifiers;
    }

    @Override
    public Iterator<OrderSpecifier> iterator() {
        return orderSpecifiers.iterator();
    }

    public boolean isUnsorted() {
        return this.orderSpecifiers.isEmpty();
    }

    public boolean isSorted() {
        return !isUnsorted();
    }

    public OrderSpecifier[] orders() {
        return this.orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    public static DslSort unsorted() {
        return UNSORTED;
    }
}
