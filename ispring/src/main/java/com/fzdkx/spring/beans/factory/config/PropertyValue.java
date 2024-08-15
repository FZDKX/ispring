package com.fzdkx.spring.beans.factory.config;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 需要依赖注入的属性
 */

public class PropertyValue {
    private String propertyName;
    private Object value;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
