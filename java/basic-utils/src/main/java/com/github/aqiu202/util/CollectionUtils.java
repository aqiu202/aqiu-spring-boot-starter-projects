package com.github.aqiu202.util;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>{@link CollectionUtils}</pre>
 *
 * @author aqiu 2020/12/13 0:05
 **/
public abstract class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

}
