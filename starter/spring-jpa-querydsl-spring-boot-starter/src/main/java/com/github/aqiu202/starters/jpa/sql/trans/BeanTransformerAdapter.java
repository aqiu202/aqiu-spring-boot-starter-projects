package com.github.aqiu202.starters.jpa.sql.trans;

import com.github.aqiu202.starters.jpa.type.DefaultTypeConverter;
import com.github.aqiu202.starters.jpa.type.TypeConverter;
import com.github.aqiu202.util.BeanUtils;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.StringUtils;
import com.github.aqiu202.util.bean.JavaBeanMethod;
import com.github.aqiu202.util.wrap.ObjectWrapper;
import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.transform.ResultTransformer;

/**
 * <pre>自定义BeanTransformerAdapter</pre>
 *
 * @author AQIU 2018/11/28 1:42 PM
 **/
public final class BeanTransformerAdapter<T> implements ResultTransformer {

    private final TypeConverter typeConverter = DefaultTypeConverter.getDefaultTypeConverter();

    private final Class<T> resultClass;
    private boolean isInitialized;
    private Setter[] setters;
    private JavaBeanMethod[] methods;
    private final Map<String, JavaBeanMethod> methodMappings = new HashMap<>();

    private BeanTransformerAdapter(Class<T> resultClass) {
        if (resultClass == null) {
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        this.isInitialized = false;
        this.resultClass = resultClass;
    }

    public static <T> BeanTransformerAdapter<T> of(Class<T> resultClass) {
        return new BeanTransformerAdapter<>(resultClass);
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;

        try {
            if (!isInitialized) {
                beforeInitialize();
                initialize(aliases);
            }

            result = resultClass.newInstance();

            for (int i = 0; i < aliases.length; i++) {
                Object value = tuple[i];
                JavaBeanMethod method = this.methods[i];
                if (method != null) {
                    Class<?> type = method.getType();
                    ObjectWrapper wrapper;
                    if (value != null && ClassUtils.notAssignableFrom(type, value.getClass())) {
                        wrapper = new ObjectWrapper(value);
                        typeConverter.convert(wrapper, type);
                        value = wrapper.get();
                    }
                    Setter setter = setters[i];
                    if (setter != null) {
                        setter.set(result, value, null);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HibernateException(
                "Could not instantiate resultclass: " + resultClass.getName());
        }

        return result;
    }

    public Class<T> getResultClass() {
        return resultClass;
    }

    private void initialize(String[] aliases) {
        PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
            PropertyAccessStrategyBasicImpl.INSTANCE,
            PropertyAccessStrategyFieldImpl.INSTANCE,
            PropertyAccessStrategyMapImpl.INSTANCE
        );
        int size = aliases.length;
        setters = new Setter[size];
        this.methods = new JavaBeanMethod[size];
        for (int i = 0; i < size; i++) {
            String alias = aliases[i];
            if (null != alias) {
                JavaBeanMethod method = this.methodMappings.get(alias.toLowerCase());
                if (method == null) {
                    continue;
                }
                this.methods[i] = method;
                setters[i] = propertyAccessStrategy
                    .buildPropertyAccess(resultClass, method.getFieldName())
                    .getSetter();
            }
        }
        isInitialized = true;
    }

    private void beforeInitialize() {
        List<JavaBeanMethod> ms = BeanUtils.getMethods(this.resultClass);
        if (!this.methodMappings.isEmpty()) {
            this.methodMappings.clear();
        }
        for (JavaBeanMethod method : ms) {
            String fieldName = method.getFieldName();
            String underscoredName = StringUtils.camelToUnderline(fieldName);
            if (fieldName.length() != underscoredName.length()) {
                this.methodMappings.put(underscoredName, method);
            }
            this.methodMappings.put(fieldName.toLowerCase(), method);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BeanTransformerAdapter that = (BeanTransformerAdapter) o;

        return resultClass.equals(that.resultClass);
    }

    @Override
    public int hashCode() {
        int result = resultClass.hashCode();
        return 31 * result;
    }

    public List transformList(List list) {
        return list;
    }
}
