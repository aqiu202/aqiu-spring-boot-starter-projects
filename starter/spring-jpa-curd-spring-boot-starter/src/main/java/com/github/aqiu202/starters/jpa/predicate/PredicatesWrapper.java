package com.github.aqiu202.starters.jpa.predicate;

import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.lambda.LambdaField;
import com.github.aqiu202.starters.jpa.lambda.LambdaFieldResolver;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class PredicatesWrapper<T, E extends KeyEntity> {

    protected final List<PredicateInfo> predicates = new ArrayList<>();

    protected abstract Root<?> getRoot();

    protected abstract CriteriaBuilder getCriteriaBuilder();

    protected Path<?> buildPath(String pathString) {
        return this.buildPath(this.getRoot(), pathString);
    }

    protected Path<?> buildPath(Root<?> root, String pathString) {
        String[] paths = pathString.split("\\.");
        Path<?> targetPath = null;
        for (String s : paths) {
            if (targetPath == null) {
                targetPath = root.get(s);
            } else {
                targetPath = targetPath.get(s);
            }
        }
        return targetPath;
    }

    private PredicateInfo buildPredicateInfo(String pathString, Logic logic, Object value) {
        return new PredicateInfo(pathString, logic, value);
    }

    protected Predicate buildPredicate(Root<?> root, PredicateInfo predicateInfo) {
        if (predicateInfo instanceof PredicateInfos) {
            PredicateInfos predicateInfos = (PredicateInfos) predicateInfo;
            Predicate[] predicates = Arrays.stream(predicateInfos.getPredicates())
                    .map(pi -> this.buildPredicate(root, pi))
                    .toArray(Predicate[]::new);
            if (predicateInfos.getConnector() == Connector.or) {
                return this.getCriteriaBuilder().or(predicates);
            } else {
                return this.getCriteriaBuilder().and(predicates);
            }
        }
        String pathString = predicateInfo.getPathString();
        Path<?> path = this.buildPath(root, pathString);
        Logic logic = predicateInfo.getLogic();
        Object value = predicateInfo.getValue();
        CriteriaBuilder criteriaBuilder = this.getCriteriaBuilder();
        switch (logic) {
            case eq:
                return criteriaBuilder.equal(path, value);
            case neq:
                return criteriaBuilder.notEqual(path, value);
            case lt:
                return criteriaBuilder.lessThan((Path<Comparable>) path, (Comparable) value);
            case gt:
                return criteriaBuilder.greaterThan((Path<Comparable>) path, (Comparable) value);
            case le:
                return criteriaBuilder.lessThanOrEqualTo((Path<Comparable>) path, (Comparable) value);
            case ge:
                return criteriaBuilder.greaterThanOrEqualTo((Path<Comparable>) path, (Comparable) value);
            case between:
                if (value.getClass().isArray()) {
                    Object[] arr = (Object[]) value;
                    return criteriaBuilder.between((Path<Comparable>) path, (Comparable) arr[0], (Comparable) arr[1]);
                }
            case notBetween:
                if (value.getClass().isArray()) {
                    Object[] arr = (Object[]) value;
                    return criteriaBuilder.between((Path<Comparable>) path, (Comparable) arr[0], (Comparable) arr[1]).not();
                }
            case like:
                return criteriaBuilder.like((Expression<String>) path, String.valueOf(value));
            case notLike:
                return criteriaBuilder.notLike((Expression<String>) path, String.valueOf(value));
            case in: {
                Class<?> valueClass = value.getClass();
                if (valueClass.isArray()) {
                    return path.in((Object[]) value);
                }
                if (value instanceof Collection) {
                    return path.in((Collection<?>) value);
                }
            }
            case notIn: {
                Class<?> valueClass = value.getClass();
                if (valueClass.isArray()) {
                    return path.in((Object[]) value).not();
                }
                if (value instanceof Collection) {
                    return path.in((Collection<?>) value).not();
                }
            }
            case isNull:
                return criteriaBuilder.isNull(path);
            case notNull:
                return criteriaBuilder.isNotNull(path);
            default: {}
        }
        return null;
    }

    public T and(Function<PredicatesWrapper<T, E>, PredicateInfo>... predicates) {
        if (predicates == null || predicates.length == 0) {
            return (T) this;
        }
        PredicateInfo[] predicateInfos = Arrays.stream(predicates)
                .map(f -> f.apply(this))
                .toArray(PredicateInfo[]::new);
        this.predicates.add(this.buildAndPredicate(predicateInfos));
        return (T) this;
    }

    public T or(Function<PredicatesWrapper<T, E>, PredicateInfo>... predicates) {
        if (predicates == null || predicates.length == 0) {
            return (T) this;
        }
        PredicateInfo[] predicateInfos = Arrays.stream(predicates)
                .map(f -> f.apply(this))
                .toArray(PredicateInfo[]::new);
        this.predicates.add(this.buildOrPredicate(predicateInfos));
        return (T) this;
    }

    public OrPredicateInfo buildOrPredicate(PredicateInfo... predicates) {
        return new OrPredicateInfo(predicates);
    }

    public AndPredicateInfo buildAndPredicate(PredicateInfo... predicates) {
        return new AndPredicateInfo(predicates);
    }

    protected String resolvePath(LambdaField<E, ?> field) {
        return LambdaFieldResolver.resolveFieldName(field);
    }

    public PredicateInfo isNull(String path) {
        return new PredicateInfo(path, Logic.isNull, null);
    }

    public PredicateInfo isNull(LambdaField<E, ?> field) {
        return this.isNull(this.resolvePath(field));
    }

    public PredicateInfo isNotNull(String path) {
        return new PredicateInfo(path, Logic.notNull, null);
    }

    public PredicateInfo isNotNull(LambdaField<E, ?> field) {
        return this.isNotNull(this.resolvePath(field));
    }

    public T andIsNull(boolean condition, String path) {
        if (condition) {
            return this.andIsNull(path);
        }
        return (T) this;
    }
    public T andIsNull(String path) {
        this.predicates.add(this.isNull(path));
        return (T) this;
    }

    public T andIsNull(boolean condition, LambdaField<E, ?> field) {
        if (condition) {
            return this.andIsNull(field);
        }
        return (T) this;
    }
    public T andIsNull(LambdaField<E, ?> field) {
        return this.andIsNull(this.resolvePath(field));
    }

    public T andIsNotNull(boolean condition, LambdaField<E, ?> field) {
        if (condition) {
            return this.andIsNotNull(field);
        }
        return (T) this;
    }

    public T andIsNotNull(LambdaField<E, ?> field) {
        return this.andIsNotNull(this.resolvePath(field));
    }

    public T andIsNotNull(boolean condition, String path) {
        if (condition) {
            return this.andIsNotNull(path);
        }
        return (T) this;
    }

    public T andIsNotNull(String path) {
        this.predicates.add(this.isNotNull(path));
        return (T) this;
    }

    private T and(String pathString, Logic logic, Object value) {
        return this.and(this.buildPredicateInfo(pathString, logic, value));
    }

    public T andEqual(boolean condition, String path, Object value) {
        if (condition) {
            return this.andEqual(path, value);
        }
        return (T) this;
    }

    public T andEqual(String path, Object value) {
        return this.and(path, Logic.eq, value);
    }

    public <V> T andEqual(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andEqual(field, value);
        }
        return (T) this;
    }

    public <V> T andEqual(LambdaField<E, V> field, V value) {
        return this.andEqual(this.resolvePath(field), value);
    }

    public T andNotEqual(boolean condition, String path, Object value) {
        if (condition) {
            return this.andNotEqual(path, value);
        }
        return (T) this;
    }

    public T andNotEqual(String path, Object value) {
        return this.and(path, Logic.neq, value);
    }

    public <V> T andNotEqual(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andNotEqual(field, value);
        }
        return (T) this;
    }

    public <V> T andNotEqual(LambdaField<E, V> field, V value) {
        return this.andNotEqual(this.resolvePath(field), value);
    }

    public T andLessThan(boolean condition, String path, Comparable<?> value) {
        if (condition) {
            return this.andLessThan(path, value);
        }
        return (T) this;
    }

    public T andLessThan(String path, Comparable<?> value) {
        return this.and(path, Logic.lt, value);
    }

    public <V extends Comparable<?>> T andLessThan(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andLessThan(field, value);
        }
        return (T) this;
    }

    public <V extends Comparable<?>> T andLessThan(LambdaField<E, V> field, V value) {
        return this.andLessThan(this.resolvePath(field), value);
    }

    public T andLessThanOrEqual(boolean condition, String path, Comparable<?> value) {
        if (condition) {
            return this.andLessThanOrEqual(path, value);
        }
        return (T) this;
    }

    public T andLessThanOrEqual(String path, Comparable<?> value) {
        return this.and(path, Logic.le, value);
    }

    public <V extends Comparable<?>> T andLessThanOrEqual(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andLessThanOrEqual(field, value);
        }
        return (T) this;
    }

    public <V extends Comparable<?>> T andLessThanOrEqual(LambdaField<E, V> field, V value) {
        return this.andLessThanOrEqual(this.resolvePath(field), value);
    }

    public T andGreaterThan(boolean condition, String path, Comparable<?> value) {
        if (condition) {
            return this.andGreaterThan(path, value);
        }
        return (T) this;
    }

    public T andGreaterThan(String path, Comparable<?> value) {
        return this.and(path, Logic.gt, value);
    }

    public <V extends Comparable<?>> T andGreaterThan(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andGreaterThan(field, value);
        }
        return (T) this;
    }

    public <V extends Comparable<?>> T andGreaterThan(LambdaField<E, V> field, V value) {
        return this.andGreaterThan(this.resolvePath(field), value);
    }

    public T andGreaterThanOrEqual(boolean condition, String path, Comparable<?> value) {
        if (condition) {
            return this.andGreaterThanOrEqual(path, value);
        }
        return (T) this;
    }

    public T andGreaterThanOrEqual(String path, Comparable<?> value) {
        return this.and(path, Logic.ge, value);
    }

    public <V extends Comparable<?>> T andGreaterThanOrEqual(boolean condition, LambdaField<E, V> field, V value) {
        if (condition) {
            return this.andGreaterThanOrEqual(field, value);
        }
        return (T) this;
    }

    public <V extends Comparable<?>> T andGreaterThanOrEqual(LambdaField<E, V> field, V value) {
        return this.andGreaterThanOrEqual(this.resolvePath(field), value);
    }

    public T andLike(boolean condition, String path, String value) {
        if (condition) {
            return this.andLike(path, value);
        }
        return (T) this;
    }

    public T andLike(String path, String value) {
        return this.and(path, Logic.like, value);
    }

    public T andLike(boolean condition, LambdaField<E, ?> field, String value) {
        if (condition) {
            return this.andLike(field, value);
        }
        return (T) this;
    }

    public T andLike(LambdaField<E, ?> field, String value) {
        return this.andLike(this.resolvePath(field), value);
    }

    public T andNotLike(boolean condition, String path, String value) {
        if (condition) {
            return this.andNotLike(path, value);
        }
        return (T) this;
    }

    public T andNotLike(String path, String value) {
        return this.and(path, Logic.notLike, value);
    }

    public T andNotLike(boolean condition, LambdaField<E, ?> field, String value) {
        if (condition) {
            return this.andNotLike(field, value);
        }
        return (T) this;
    }

    public T andNotLike(LambdaField<E, ?> field, String value) {
        return this.andNotLike(this.resolvePath(field), value);
    }

    public T andIn(boolean condition, String path, Collection<?> value) {
        if (condition) {
            return this.andIn(path, value);
        }
        return (T) this;
    }

    public T andIn(String path, Collection<?> value) {
        return this.and(path, Logic.in, value);
    }

    public <V> T andIn(boolean condition, LambdaField<E, V> field, Collection<V> value) {
        if (condition) {
            return this.andIn(field, value);
        }
        return (T) this;
    }

    public <V> T andIn(LambdaField<E, V> field, Collection<V> value) {
        return this.andIn(this.resolvePath(field), value);
    }

    public T andNotIn(boolean condition, String path, Collection<?> value) {
        if (condition) {
            return this.andNotIn(path, value);
        }
        return (T) this;
    }

    public T andNotIn(String path, Collection<?> value) {
        return this.and(path, Logic.notIn, value);
    }

    public <V> T andNotIn(boolean condition, LambdaField<E, V> field, Collection<V> value) {
        if (condition) {
            return this.andNotIn(field, value);
        }
        return (T) this;
    }

    public <V> T andNotIn(LambdaField<E, V> field, Collection<V> value) {
        return this.andNotIn(this.resolvePath(field), value);
    }

    public T andBetween(boolean condition, String path, Object begin, Object end) {
        if (condition) {
            return this.andBetween(path, begin, end);
        }
        return (T) this;
    }

    public T andBetween(String path, Object begin, Object end) {
        return this.and(path, Logic.between, new Object[]{begin, end});
    }

    public <V> T andBetween(boolean condition, LambdaField<E, V> field, V begin, V end) {
        if (condition) {
            return this.andBetween(field, begin, end);
        }
        return (T) this;
    }

    public <V> T andBetween(LambdaField<E, V> field, V begin, V end) {
        return this.andBetween(this.resolvePath(field), begin, end);
    }

    public T andNotBetween(boolean condition, String path, Object begin, Object end) {
        if (condition) {
            return this.andNotBetween(path, begin, end);
        }
        return (T) this;
    }

    public T andNotBetween(String path, Object begin, Object end) {
        return this.and(path, Logic.notBetween, new Object[]{begin, end});
    }

    public <V> T andNotBetween(boolean condition, LambdaField<E, V> field, V begin, V end) {
        if (condition) {
            return this.andNotBetween(field, begin, end);
        }
        return (T) this;
    }

    public <V> T andNotBetween(LambdaField<E, V> field, V begin, V end) {
        return this.andNotBetween(this.resolvePath(field), begin, end);
    }

    public PredicateInfo isEqual(String path, Object value) {
        return this.buildPredicateInfo(path, Logic.eq, value);
    }

    public <V> PredicateInfo isEqual(LambdaField<E, V> field, V value) {
        return this.isEqual(this.resolvePath(field), value);
    }

    public PredicateInfo isNotEqual(String path, Object value) {
        return this.buildPredicateInfo(path, Logic.neq, value);
    }

    public <V> PredicateInfo isNotEqual(LambdaField<E, V> field, V value) {
        return this.isNotEqual(this.resolvePath(field), value);
    }

    public PredicateInfo isLessThan(String path, Comparable<?> value) {
        return this.buildPredicateInfo(path, Logic.lt, value);
    }

    public <V extends Comparable<?>> PredicateInfo isLessThan(LambdaField<E, V> field, V value) {
        return this.isLessThan(this.resolvePath(field), value);
    }

    public PredicateInfo isLessThanOrEqual(String path, Comparable<?> value) {
        return this.buildPredicateInfo(path, Logic.le, value);
    }

    public <V extends Comparable<?>> PredicateInfo isLessThanOrEqual(LambdaField<E, V> field, V value) {
        return this.isLessThanOrEqual(this.resolvePath(field), value);
    }

    public PredicateInfo isGreaterThan(String path, Comparable<?> value) {
        return this.buildPredicateInfo(path, Logic.gt, value);
    }

    public <V extends Comparable<?>> PredicateInfo isGreaterThan(LambdaField<E, V> field, V value) {
        return this.isGreaterThan(this.resolvePath(field), value);
    }

    public PredicateInfo isGreaterThanOrEqual(String path, Comparable<?> value) {
        return this.buildPredicateInfo(path, Logic.ge, value);
    }

    public <V extends Comparable<?>> PredicateInfo isGreaterThanOrEqual(LambdaField<E, V> field, V value) {
        return this.isGreaterThanOrEqual(this.resolvePath(field), value);
    }

    public PredicateInfo isLike(String path, String value) {
        return this.buildPredicateInfo(path, Logic.like, value);
    }

    public PredicateInfo isLike(LambdaField<E, ?> field, String value) {
        return this.isLike(this.resolvePath(field), value);
    }

    public PredicateInfo isNotLike(String path, String value) {
        return this.buildPredicateInfo(path, Logic.notLike, value);
    }
    
    public PredicateInfo isNotLike(LambdaField<E, ?> field, String value) {
        return this.isNotLike(this.resolvePath(field), value);
    }

    public PredicateInfo isIn(String path, Collection<?> value) {
        return this.buildPredicateInfo(path, Logic.in, value);
    }
    
    public <V> PredicateInfo isIn(LambdaField<E, V> field, Collection<V> value) {
        return this.isIn(this.resolvePath(field), value);
    }

    public PredicateInfo isNotIn(String path, Collection<?> value) {
        return this.buildPredicateInfo(path, Logic.notIn, value);
    }
    
    public <V> PredicateInfo isNotIn(LambdaField<E, V> field, Collection<V> value) {
        return this.isNotIn(this.resolvePath(field), value);
    }

    public PredicateInfo isBetween(String path, Object begin, Object end) {
        return this.buildPredicateInfo(path, Logic.between, new Object[]{begin, end});
    }
    
    public <V> PredicateInfo isBetween(LambdaField<E, V> field, V begin, V end) {
        return this.isBetween(this.resolvePath(field), begin, end);
    }

    public PredicateInfo isNotBetween(String path, Object begin, Object end) {
        return this.buildPredicateInfo(path, Logic.notBetween, new Object[]{begin, end});
    }
    
    public <V> PredicateInfo isNotBetween(LambdaField<E, V> field, V begin, V end) {
        return this.isNotBetween(this.resolvePath(field), begin, end);
    }

    private T and(PredicateInfo predicate) {
        this.predicates.add(predicate);
        return (T) this;
    }

    public List<PredicateInfo> getPredicates() {
        return this.predicates;
    }

    public List<Predicate> buildPredicates() {
        return this.buildPredicates(this.getRoot());
    }

    public List<Predicate> buildPredicates(Root<?> root) {
        return this.getPredicates().stream().map(pi -> this.buildPredicate(root, pi))
                .collect(Collectors.toList());
    }

    public enum Logic {
        eq,
        neq,
        ge,
        gt,
        le,
        lt,
        like,
        notLike,
        between,
        notBetween,
        in,
        notIn,
        isNull,
        notNull
    }

    public enum Connector {
        and,
        or
    }
}
