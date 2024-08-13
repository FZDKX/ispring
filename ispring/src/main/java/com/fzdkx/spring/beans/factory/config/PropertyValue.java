package com.fzdkx.spring.beans.factory.config;

import lombok.Data;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 需要依赖注入的属性
 */
@Data
public class PropertyValue {
    private String propertyName;
    private Object value;

}
