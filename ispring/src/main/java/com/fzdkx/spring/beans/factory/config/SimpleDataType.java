package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeanInstanceException;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class SimpleDataType {
    // 定义基本数据类型数组
    public static Class<?>[] getSimpleDataType() {
        return new Class[]{
                Byte.class, Short.class, Integer.class, Long.class,
                Float.class, Double.class, Boolean.class, Character.class
        };
    }

    public static Object convert(String value, Class<?> clazz) {
        if (clazz == Byte.class) {
            return new Byte(value);
        } else if (clazz == Short.class) {
            return new Short(value);
        } else if (clazz == Integer.class) {
            return new Integer(value);
        } else if (clazz == Long.class) {
            return new Long(value);
        } else if (clazz == Float.class) {
            return new Float(value);
        } else if (clazz == Double.class) {
            return new Double(value);
        } else if (clazz == Boolean.class) {
            return Boolean.valueOf(value);
        } else if (clazz == Character.class) {
            if (value.length() == 1) {
                return value.charAt(0);
            }
            throw new BeanInstanceException("转换异常");
        }
        throw new BeanInstanceException("转换异常");
    }
}
