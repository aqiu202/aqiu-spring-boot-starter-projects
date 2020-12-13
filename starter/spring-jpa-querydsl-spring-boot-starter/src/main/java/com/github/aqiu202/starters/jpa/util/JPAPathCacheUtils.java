package com.github.aqiu202.starters.jpa.util;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.github.aqiu202.util.BeanUtils;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/23 16:04
 **/
public final class JPAPathCacheUtils {

    private JPAPathCacheUtils() {
    }

    private static final Map<String, List<Field>> fieldsCache = new ConcurrentHashMap<>();

    private static final String _super = "_super";

    public static List<Field> getSelfPublicFields(Class<?> clz) {
        FieldAccess fieldAccess = BeanUtils.getFieldAccess(clz);
        Field[] fields = fieldAccess.getFields();
        List<Field> fieldList;
        String key = clz.getName();
        if ((fieldList = fieldsCache.get(key)) == null) {
            fieldList = new ArrayList<>();
            for (Field field : fields) {
                int m = field.getModifiers();
                if (!_super.equals(field.getName()) && !Modifier.isStatic(m)
                        && Modifier.isPublic(m)) {
                    fieldList.add(field);
                }
            }
            fieldsCache.putIfAbsent(key, fieldList);
        }
        return fieldList;
    }

    public static List<Path<?>> getEntityPaths(EntityPathBase<?> entityPathBase) {
        Class<?> type = entityPathBase.getClass();
        List<Field> fields = getSelfPublicFields(type);
        return fields.stream().map(f -> {
            try {
                return (Path<?>) f.get(entityPathBase);
            } catch (IllegalAccessException ignored) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<Expression<?>> getExpressions(Expression<?>... exprs) {
        return getExpressions(Arrays.asList(exprs));
    }

    public static List<Expression<?>> getExpressions(Iterable<Expression<?>> exprs) {
        List<Expression<?>> result = new ArrayList<>();
        for (Expression<?> expr : exprs) {
            if (expr instanceof EntityPathBase) {
                List<Path<?>> entityPaths = getEntityPaths((EntityPathBase<?>) expr);
                result.addAll(entityPaths);
            } else {
                result.add(expr);
            }
        }
        return result;
    }

}
