package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.starters.jpa.util.JPAPathCacheUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Visitor;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/26 0:07
 **/
public class JPAQMaps extends FactoryExpressionBase<Map<Expression<?>, ?>> {

    private static final long serialVersionUID = -7545994090073480810L;

    private final ImmutableList<Expression<?>> args;

    /**
     * Create a new QMap instance
     *
     * @param args 参数
     */
    @SuppressWarnings("unchecked")
    protected JPAQMaps(Expression<?>... args) {
        super((Class) Map.class);
        this.args = ImmutableList.copyOf(JPAPathCacheUtils.getExpressions(args));
    }

    /**
     * Create a new QMap instance
     *
     * @param args 参数
     */
    @SuppressWarnings("unchecked")
    protected JPAQMaps(ImmutableList<Expression<?>> args) {
        super((Class) Map.class);
        this.args = ImmutableList.copyOf(JPAPathCacheUtils.getExpressions(args));
    }

    /**
     * Create a new QMap instance
     *
     * @param args 参数
     */
    @SuppressWarnings("unchecked")
    protected JPAQMaps(Expression<?>[]... args) {
        super((Class) Map.class);
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?>[] exprs : args) {
            builder.addAll(JPAPathCacheUtils.getExpressions(exprs));
        }
        this.args = builder.build();
    }

    @Override
    @Nullable
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
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
    @Nullable
    public Map<Expression<?>, ?> newInstance(Object... args) {
        Map<Expression<?>, Object> map = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            map.put(this.args.get(i), args[i]);
        }
        return map;
    }

}
