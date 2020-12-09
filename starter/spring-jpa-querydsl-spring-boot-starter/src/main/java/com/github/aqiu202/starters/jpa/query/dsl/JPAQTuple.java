package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.starters.jpa.util.JPAPathCacheUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Visitor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/26 9:50
 **/
public class JPAQTuple extends FactoryExpressionBase<Tuple> {

    private static ImmutableMap<Expression<?>, Integer> createBindings(List<Expression<?>> exprs) {
        Map<Expression<?>, Integer> map = Maps.newHashMap();
        for (int i = 0; i < exprs.size(); i++) {
            Expression<?> e = exprs.get(i);
            if (e instanceof Operation && ((Operation<?>) e).getOperator() == Ops.ALIAS) {
                map.put(((Operation<?>) e).getArg(1), i);
            }
            map.put(e, i);
        }
        return ImmutableMap.copyOf(map);
    }

    private final class TupleImpl implements Tuple, Serializable {

        private static final long serialVersionUID = 6635924689293325950L;

        private final Object[] a;

        private TupleImpl(Object[] a) {
            this.a = a;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) a[index];
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Expression<T> expr) {
            Integer idx = JPAQTuple.this.bindings.get(expr);
            if (idx != null) {
                return (T) a[idx];
            } else {
                return null;
            }
        }

        @Override
        public int size() {
            return a.length;
        }

        @Override
        public Object[] toArray() {
            return a;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof Tuple) {
                return Arrays.equals(a, ((Tuple) obj).toArray());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(a);
        }

        @Override
        public String toString() {
            return Arrays.toString(a);
        }
    }

    private static final long serialVersionUID = -2640616030595420465L;

    private final ImmutableList<Expression<?>> args;

    private final ImmutableMap<Expression<?>, Integer> bindings;

    /**
     * Create a new JPAQTuple instance
     *
     * @param args 参数
     */
    protected JPAQTuple(Expression<?>... args) {
        super(Tuple.class);
        this.args = ImmutableList.copyOf(JPAPathCacheUtils.getExpressions(args));
        this.bindings = createBindings(this.args);
    }

    /**
     * Create a new JPAQTuple instance
     *
     * @param args 参数
     */
    protected JPAQTuple(ImmutableList<Expression<?>> args) {
        super(Tuple.class);
        this.args = ImmutableList.copyOf(JPAPathCacheUtils.getExpressions(args));
        this.bindings = createBindings(this.args);
    }

    /**
     * Create a new JPAQTuple instance
     *
     * @param args 参数
     */
    protected JPAQTuple(Expression<?>[]... args) {
        super(Tuple.class);
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?>[] exprs : args) {
            builder.addAll(JPAPathCacheUtils.getExpressions(exprs));
        }
        this.args = builder.build();
        this.bindings = createBindings(this.args);
    }

    @Override
    public Tuple newInstance(Object... a) {
        return new TupleImpl(a);
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FactoryExpression) {
            FactoryExpression<?> c = (FactoryExpression<?>) obj;
            return args.equals(c.getArgs()) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }
}
