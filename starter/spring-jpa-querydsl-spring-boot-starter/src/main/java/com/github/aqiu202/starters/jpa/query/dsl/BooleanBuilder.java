package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.util.StringUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * <pre>自定义JPA查询条件构造Builder</pre>
 *
 * @author aqiu 2020/11/17 13:13
 **/
public class BooleanBuilder implements Predicate, Cloneable {

    @Nullable
    private Predicate predicate;

    /**
     * Create an empty BooleanBuilder
     */
    public BooleanBuilder() {
    }

    /**
     * Create a BooleanBuilder with the given initial value
     *
     * @param initial initial value
     */
    public BooleanBuilder(Predicate initial) {
        predicate = (Predicate) ExpressionUtils.extract(initial);
    }

    public static BooleanBuilder builder(Predicate initial) {
        return new BooleanBuilder(initial);
    }

    public static BooleanBuilder builder() {
        return new BooleanBuilder();
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        if (predicate != null) {
            return predicate.accept(v, context);
        } else {
            return null;
        }
    }

    public <T extends Comparable<?>> BooleanBuilder notNullAndCompare(
            ComparableExpression<T> expression,
            @Nullable T value,
            BiFunction<ComparableExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterAndCompare(expression, value, Objects::nonNull, compareFun);
    }

    public <T extends Comparable<?>> BooleanBuilder notNullOrCompare(
            ComparableExpression<T> expression,
            @Nullable T value,
            BiFunction<ComparableExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterOrCompare(expression, value, Objects::nonNull, compareFun);
    }

    public BooleanBuilder notEmptyAndEq(StringExpression expression,
                                        @Nullable String value) {
        return this.filterAndEq(expression, value, StringUtils::isNotEmpty);
    }

    public BooleanBuilder notEmptyOrEq(StringExpression expression,
                                       @Nullable String value) {
        return this.filterOrEq(expression, value, StringUtils::isNotEmpty);
    }

    public BooleanBuilder notBlankAndEq(StringExpression expression,
                                        @Nullable String value) {
        return this.filterAndEq(expression, value, StringUtils::hasText);
    }

    public BooleanBuilder notBlankOrEq(StringExpression expression,
                                       @Nullable String value) {
        return this.filterOrEq(expression, value, StringUtils::hasText);
    }

    public <T> BooleanBuilder notNullAndEq(
            SimpleExpression<T> expression, @Nullable T value) {
        return this.filterAndEq(expression, value, Objects::nonNull);
    }

    public <T> BooleanBuilder notNullOrEq(
            SimpleExpression<T> expression, @Nullable T value) {
        return this.filterOrEq(expression, value, Objects::nonNull);
    }

    public BooleanBuilder notEmptyAndLike(
            StringExpression expression, @Nullable String value) {
        return this.filterAndLike(expression, value, StringUtils::isNotEmpty);
    }

    public BooleanBuilder notBlankAndLike(
            StringExpression expression, @Nullable String value) {
        return this.filterAndLike(expression, value, StringUtils::hasText);
    }

    public BooleanBuilder notEmptyOrLike(
            StringExpression expression, @Nullable String value) {
        return this.filterOrLike(expression, value, StringUtils::isNotEmpty);
    }

    public BooleanBuilder notBlankOrLike(
            StringExpression expression, @Nullable String value) {
        return this.filterOrLike(expression, value, StringUtils::hasText);
    }

    public BooleanBuilder filterAndLike(
            StringExpression expression, @Nullable String value,
            java.util.function.Predicate<String> predicate) {
        Optional.ofNullable(value).filter(predicate)
                .ifPresent(v -> this.and(expression.like(this.likeValue(v))));
        return this;
    }

    public BooleanBuilder filterOrLike(
            StringExpression expression, @Nullable String value,
            java.util.function.Predicate<String> predicate) {
        Optional.ofNullable(value).filter(predicate)
                .ifPresent(v -> this.or(expression.like(this.likeValue(v))));
        return this;
    }

    public <T> BooleanBuilder filterAndEq(
            SimpleExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate) {
        return this.filterAndHandle(expression, value, predicate, SimpleExpression::eq);
    }

    public <T> BooleanBuilder filterOrEq(
            SimpleExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate) {
        return this.filterOrHandle(expression, value, predicate, SimpleExpression::eq);
    }

    public <T extends Comparable<?>> BooleanBuilder filterAndCompare(
            ComparableExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<ComparableExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterThenCompare(expression, value, predicate, compareFun,
                BooleanBuilder::and);
    }

    public <T extends Comparable<?>> BooleanBuilder filterOrCompare(
            ComparableExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<ComparableExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterThenCompare(expression, value, predicate, compareFun,
                BooleanBuilder::or);
    }

    public <T> BooleanBuilder filterAndHandle(
            SimpleExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<SimpleExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterThenHandle(expression, value, predicate, compareFun,
                BooleanBuilder::and);
    }

    public <T> BooleanBuilder filterAndHandle(
            SimpleExpression<T> expression, @Nullable Collection<T> value,
            java.util.function.Predicate<Collection<T>> predicate,
            BiFunction<SimpleExpression<T>, Collection<T>, BooleanExpression> compareFun) {
        return this.filterThenHandle(expression, value, predicate, compareFun,
                BooleanBuilder::and);
    }

    public <T> BooleanBuilder filterOrHandle(
            SimpleExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<SimpleExpression<T>, T, BooleanExpression> compareFun) {
        return this.filterThenHandle(expression, value, predicate, compareFun,
                BooleanBuilder::or);
    }

    public <T extends Comparable<?>> BooleanBuilder filterThenCompare(
            ComparableExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<ComparableExpression<T>, T, BooleanExpression> compareFun,
            BiFunction<BooleanBuilder, Predicate, BooleanBuilder> function) {
        Optional.ofNullable(value).filter(predicate)
                .ifPresent(v -> function.apply(this, compareFun.apply(expression, v)));
        return this;
    }

    public <T> BooleanBuilder filterThenHandle(
            SimpleExpression<T> expression, @Nullable T value,
            java.util.function.Predicate<T> predicate,
            BiFunction<SimpleExpression<T>, T, BooleanExpression> compareFun,
            BiFunction<BooleanBuilder, Predicate, BooleanBuilder> function) {
        Optional.ofNullable(value).filter(predicate)
                .ifPresent(v -> function.apply(this, compareFun.apply(expression, v)));
        return this;
    }

    public <T> BooleanBuilder filterThenHandle(
            SimpleExpression<T> expression, @Nullable Collection<T> value,
            java.util.function.Predicate<Collection<T>> predicate,
            BiFunction<SimpleExpression<T>, Collection<T>, BooleanExpression> compareFun,
            BiFunction<BooleanBuilder, Predicate, BooleanBuilder> function) {
        Optional.ofNullable(value).filter(predicate)
                .ifPresent(v -> function.apply(this, compareFun.apply(expression, v)));
        return this;
    }

    public <T extends Comparable<?>> BooleanBuilder notNullAndBetween(
            ComparableExpression<T> expression, @Nullable T value1, @Nullable T value2) {
        return this.notNullThenBetween(expression, value1, value2, BooleanBuilder::and);
    }

    public <T extends Comparable<?>> BooleanBuilder notNullOrBetween(
            ComparableExpression<T> expression, @Nullable T value1, @Nullable T value2) {
        return this.notNullThenBetween(expression, value1, value2, BooleanBuilder::or);
    }


    public <T extends Comparable<?>> BooleanBuilder notNullThenBetween(
            ComparableExpression<T> expression, @Nullable T value1, @Nullable T value2,
            BiFunction<BooleanBuilder, Predicate, BooleanBuilder> function) {
        if (value1 != null && value2 != null) {
            function.apply(this, expression.between(value1, value2));
        } else if (value1 != null) {
            function.apply(this, expression.goe(value1));
        } else if (value2 != null) {
            function.apply(this, expression.loe(value2));
        }
        return this;
    }

    /**
     * Create the intersection of this and the given predicate
     *
     * @param right right hand side of {@code and} operation
     * @return the current object
     */
    public BooleanBuilder and(@Nullable Predicate right) {
        if (right != null) {
            if (predicate == null) {
                predicate = right;
            } else {
                predicate = ExpressionUtils.and(predicate, right);
            }
        }
        return this;
    }

    private String likeValue(String value) {
        return "%" + value + "%";
    }

    /**
     * Create the intersection of this and the union of the given args {@code (this && (arg1 || arg2
     * ... || argN))}
     *
     * @param args union of predicates
     * @return the current object
     */
    public BooleanBuilder andAnyOf(Predicate... args) {
        if (args.length > 0) {
            and(ExpressionUtils.anyOf(args));
        }
        return this;
    }

    /**
     * Create the insertion of this and the negation of the given predicate
     *
     * @param right predicate to be negated
     * @return the current object
     */
    public BooleanBuilder andNot(Predicate right) {
        return and(right.not());
    }

    @Override
    public BooleanBuilder clone() throws CloneNotSupportedException {
        return (BooleanBuilder) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof com.querydsl.core.BooleanBuilder) {
            return Objects.equals(((com.querydsl.core.BooleanBuilder) o).getValue(), predicate);
        } else {
            return false;
        }
    }

    @Nullable
    public Predicate getValue() {
        return predicate;
    }

    @Override
    public int hashCode() {
        return predicate != null ? predicate.hashCode() : 0;
    }

    /**
     * Returns true if the value is set, and false, if not
     *
     * @return true if initialized and false if not
     */
    public boolean hasValue() {
        return predicate != null;
    }

    @Override
    public BooleanBuilder not() {
        if (predicate != null) {
            predicate = predicate.not();
        }
        return this;
    }

    /**
     * Create the union of this and the given predicate
     *
     * @param right right hand side of {@code or} operation
     * @return the current object
     */
    public BooleanBuilder or(@Nullable Predicate right) {
        if (right != null) {
            if (predicate == null) {
                predicate = right;
            } else {
                predicate = ExpressionUtils.or(predicate, right);
            }
        }
        return this;
    }

    /**
     * Create the union of this and the intersection of the given args {@code (this || (arg1 && arg2
     * ... && argN))}
     *
     * @param args intersection of predicates
     * @return the current object
     */
    public BooleanBuilder orAllOf(Predicate... args) {
        if (args.length > 0) {
            or(ExpressionUtils.allOf(args));
        }
        return this;
    }

    /**
     * Create the union of this and the negation of the given predicate
     *
     * @param right predicate to be negated
     * @return the current object
     */
    public BooleanBuilder orNot(Predicate right) {
        return or(right.not());
    }

    @Override
    public Class<? extends Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String toString() {
        return predicate != null ? predicate.toString() : super.toString();
    }

}
