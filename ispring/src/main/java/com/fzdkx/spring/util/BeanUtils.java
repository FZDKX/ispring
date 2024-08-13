package com.fzdkx.spring.util;

import com.fzdkx.spring.beans.factory.config.SimpleDataType;
import com.fzdkx.spring.beans.exception.PropertyInjectException;

import java.lang.reflect.Field;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class BeanUtils {

    // 无转换
    public static void setFieldValue(Object bean, String fieldName, Object value) {
        Class<?> clazz = bean.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new PropertyInjectException("属性注入失败");
        }
    }

    // 有转换
    public static void convertAndSetFieldValue(Object bean, String fieldName, Object value) {

        Class<?> clazz = bean.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            value = SimpleDataType.convert((String) value, field.getType());
            field.setAccessible(true);
            field.set(bean, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new PropertyInjectException("属性注入失败");
        }
    }
}
