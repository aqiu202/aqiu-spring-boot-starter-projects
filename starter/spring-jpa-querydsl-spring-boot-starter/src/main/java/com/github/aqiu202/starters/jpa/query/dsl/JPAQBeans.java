/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.starters.jpa.type.DefaultTypeConverter;
import com.github.aqiu202.starters.jpa.type.TypeConverter;
import com.github.aqiu202.starters.jpa.util.JPAPathCacheUtils;
import com.github.aqiu202.starters.jpa.wrap.ObjectWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionException;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.EntityPathBase;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * JPA QueryDsl 结果自定义类型转换类
 * @author aqiu 2019-10-06 20:24
 **/
public class JPAQBeans<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -8210214512730989778L;

    private final boolean fieldAccess;

    private final TypeConverter typeConverter = DefaultTypeConverter.getDefaultTypeConverter();

    private final ImmutableMap<String, Expression<?>> bindings;

    private final Method[] setters;

    private final Field[] fields;

    /**
     * Create a new QBean instance
     *
     * @param type type of bean
     * @param args properties to be populated
     */
    protected JPAQBeans(Class<? extends T> type, Expression<?>... args) {
        this(type, createBindings(args));
    }

    protected JPAQBeans(Class<? extends T> type, Map<String, ? extends Expression<?>> bindings) {
        this(type, false, bindings);
    }

    protected JPAQBeans(Class<? extends T> type, boolean fieldAccess, Expression<?>... args) {
        this(type, fieldAccess, createBindings(args));
    }

    /**
     * Create a new QBean instance
     *
     * @param type        type of bean
     * @param fieldAccess true, for field access and false, for property access
     * @param bindings    bindings
     */
    protected JPAQBeans(Class<? extends T> type, boolean fieldAccess,
            Map<String, ? extends Expression<?>> bindings) {
        super(type);
        this.bindings = ImmutableMap.copyOf(bindings);
        this.fieldAccess = fieldAccess;
        if (fieldAccess) {
            this.fields = initFields(bindings);
            this.setters = null;
        } else {
            this.fields = null;
            this.setters = initMethods(bindings);
        }
    }

    private static ImmutableMap<String, Expression<?>> createBindings(Expression<?>... args) {
        ImmutableMap.Builder<String, Expression<?>> rv = ImmutableMap.builder();
        for (Expression<?> expr : args) {
            //查询实体类会默认使用反射解析所有字段，并会缓存所有字段
            if (expr instanceof EntityPathBase) {
                List<Path<?>> entityPaths = JPAPathCacheUtils
                        .getEntityPaths((EntityPathBase<?>) expr);
                entityPaths.forEach(path -> rv.put(path.getMetadata().getName(), path));
            } else if (expr instanceof Path<?>) {
                Path<?> path = (Path<?>) expr;
                rv.put(path.getMetadata().getName(), expr);
            } else if (expr instanceof Operation<?>) {
                Operation<?> operation = (Operation<?>) expr;
                if (operation.getOperator() == Ops.ALIAS && operation
                        .getArg(1) instanceof Path<?>) {
                    Path<?> path = (Path<?>) operation.getArg(1);
                    if (isCompoundExpression(operation.getArg(0))) {
                        rv.put(path.getMetadata().getName(), operation.getArg(0));
                    } else {
                        rv.put(path.getMetadata().getName(), operation);
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported expression " + expr);
                }

            } else {
                throw new IllegalArgumentException("Unsupported expression " + expr);
            }
        }
        return rv.build();
    }

    private static boolean isCompoundExpression(Expression<?> expr) {
        return expr instanceof FactoryExpression || expr instanceof GroupExpression;
    }

    private static Class<?> normalize(Class<?> cl) {
        return cl.isPrimitive() ? Primitives.wrap(cl) : cl;
    }

    private static boolean notAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return !normalize(cl1).isAssignableFrom(normalize(cl2));
    }

    protected void typeMismatch(Class<?> type, Expression<?> expr) {
        final String msg = expr.getType().getName() + " is not compatible with " + type.getName();
        throw new IllegalArgumentException(msg);
    }

    protected void propertyNotFound(Expression<?> expr, String property) {
        // do nothing
    }

    private Method[] initMethods(Map<String, ? extends Expression<?>> args) {
        try {
            Method[] methods = new Method[args.size()];
            BeanInfo beanInfo = Introspector.getBeanInfo(getType());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            int i = 0;
            for (Map.Entry<String, ? extends Expression<?>> entry : args.entrySet()) {
                String property = entry.getKey();
                Expression<?> expr = entry.getValue();
                Method setter = null;
                for (PropertyDescriptor prop : propertyDescriptors) {
                    if (prop.getName().equals(property)) {
                        setter = prop.getWriteMethod();
                        if (notAssignableFrom(prop.getPropertyType(), expr.getType())) {
                            typeMismatch(prop.getPropertyType(), expr);
                        }
                        break;
                    }
                }
                if (setter == null) {
                    propertyNotFound(expr, property);
                }
                methods[i++] = setter;
            }
            return methods;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Field[] initFields(Map<String, ? extends Expression<?>> args) {
        Field[] fields = new Field[args.size()];
        int i = 0;
        for (Map.Entry<String, ? extends Expression<?>> entry : args.entrySet()) {
            String property = entry.getKey();
            Expression<?> expr = entry.getValue();
            Class<?> beanType = getType();
            Field field = null;
            while (!beanType.equals(Object.class)) {
                try {
                    field = beanType.getDeclaredField(property);
                    field.setAccessible(true);
                    if (notAssignableFrom(field.getType(), expr.getType())) {
                        typeMismatch(field.getType(), expr);
                    }
                    beanType = Object.class;
                } catch (SecurityException e) {
                    // do nothing
                } catch (NoSuchFieldException e) {
                    beanType = beanType.getSuperclass();
                }
            }
            if (field == null) {
                propertyNotFound(expr, property);
            }
            fields[i++] = field;
        }
        return fields;
    }

    @Override
    public T newInstance(Object... a) {
        try {
            T rv = create(getType());
            for (int i = 0; i < a.length; i++) {
                Object value = a[i];
                if (value != null) {
                    Class<?> type;
                    if (this.fieldAccess) {
                        Field field = this.fields[i];
                        if (field != null) {
                            type = field.getType();
                            field.set(rv, this.convertValue(value, type));
                        }
                    } else {
                        Method setter = this.setters[i];
                        if (setter != null) {
                            Class<?>[] types = setter.getParameterTypes();
                            if (types.length > 0) {
                                type = types[0];
                                value = this.convertValue(value, type);
                            }
                            setter.invoke(rv, value);
                        }
                    }
                }
            }
            return rv;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

    private Object convertValue(Object value, Class<?> type) {
        if (notAssignableFrom(type, value.getClass())) {
            ObjectWrapper wrapper = new ObjectWrapper(value);
            this.typeConverter.convert(wrapper, type);
            value = wrapper.get();
        }
        return value;
    }

    private <S> S create(Class<S> type) throws IllegalAccessException, InstantiationException {
        return type.newInstance();
    }

    public Expression<T> as(Path<T> alias) {
        return ExpressionUtils.operation(getType(), Ops.ALIAS, this, alias);
    }

    public Expression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return bindings.values().asList();
    }

}
