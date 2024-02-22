package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.model.PropertyAccessor;

public class ProxyBeanProperty implements BeanProperty {

    private final BeanProperty bp;

    public ProxyBeanProperty(Class<?> type, String propertyName, PropertyAccessor propertyAccessor) {
        switch (propertyAccessor) {
            case FIELD:
                this.bp = new FieldBeanProperty(type, propertyName);
                break;
            case METHOD:
                this.bp = new MethodBeanProperty(type, propertyName);
                break;
            default:
                throw new RuntimeException("不支持的访问方式配置");
        }
    }

    private BeanProperty getProxy() {
        return this.bp;
    }

    @Override
    public Class<?> getBeanType() {
        return this.getProxy().getBeanType();
    }

    @Override
    public Class<?> getPropertyType() {
        return this.getProxy().getPropertyType();
    }

    @Override
    public String getPropertyName() {
        return this.getProxy().getPropertyName();
    }

    @Override
    public void setValue(Object instance, Object value) {
        this.getProxy().setValue(instance, value);
    }

    @Override
    public Object getValue(Object instance) {
        return this.getProxy().getValue(instance);
    }

    @Override
    public boolean isValid() {
        return this.getProxy().isValid();
    }
}
