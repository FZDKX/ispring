package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeanInstanceException;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class SimpleDataType {

    static final Class<?>[] simpleType = new Class[]{
            Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Boolean.class, Character.class
    };

    // 定义基本数据类型数组
    public static Class<?>[] getSimpleDataType() {
        return simpleType;
    }

    public static <T> T parseNumber(String value, Class<T> clazz) {
        if (clazz == Byte.class) {
            return (T) new Byte(value);
        } else if (clazz == Short.class) {
            return (T) new Short(value);
        } else if (clazz == Integer.class) {
            return (T) new Integer(value);
        } else if (clazz == Long.class) {
            return (T) new Long(value);
        } else if (clazz == Float.class) {
            return (T) new Float(value);
        } else if (clazz == Double.class) {
            return (T) new Double(value);
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (clazz == Character.class) {
            if (value.length() == 1) {
                return (T) new Character(value.charAt(0));
            }
        } else if (clazz == String.class) {
            return (T) value;
        }
        throw new BeanInstanceException("转换异常");
    }

    // 判断是为简单类型
    public static boolean isSimpleType(Class<?> clazz){
        for (Class<?> aClass : simpleType) {
            if (aClass == clazz){
                return true;
            }
        }
        return false;
    }
}
