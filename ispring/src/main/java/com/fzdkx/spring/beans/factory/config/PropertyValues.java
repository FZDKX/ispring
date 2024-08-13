package com.fzdkx.spring.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addProperty(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[propertyValueList.size()]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getPropertyName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }
}
