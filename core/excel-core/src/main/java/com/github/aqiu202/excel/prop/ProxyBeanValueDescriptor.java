package com.github.aqiu202.excel.prop;

import com.github.aqiu202.excel.model.PropertyAccessor;

public class ProxyBeanValueDescriptor implements BeanValueDescriptor {

    private final BeanValueDescriptor bp;

    public ProxyBeanValueDescriptor(Class<?> type, String propertyName, PropertyAccessor propertyAccessor) {
        switch (propertyAccessor) {
            case FIELD:
                this.bp = new FieldBeanValueDescriptor(type, propertyName);
                break;
            case METHOD:
                this.bp = new MethodBeanValueDescriptor(type, propertyName);
                break;
            default:
                throw new RuntimeException("不支持的访问方式配置");
        }
    }

    private BeanValueDescriptor getProxy() {
        return this.bp;
    }

    @Override
    public Class<?> getBeanType() {
        return this.getProxy().getBeanType();
    }

    @Override
    public Class<?> getValueType() {
        return this.getProxy().getValueType();
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
