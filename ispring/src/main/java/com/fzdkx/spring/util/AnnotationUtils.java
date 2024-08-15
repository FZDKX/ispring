package com.fzdkx.spring.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/14
 */
public class AnnotationUtils {

    // 获取某个Class类上是否有指定注解
    public static Annotation getAnnotation(Class clazz, Class annotationType) {
        Annotation temp;
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType() == annotationType) {
                return annotation;
            }
            // 没有，递归
            if ((temp = getAnnotation(annotation.getClass(), annotationType)) != null) {
                return temp;
            }
            // 还是没有，下一个
        }
        return null;
    }

    public static Object getAValue(Annotation annotation, String attributeName) {
        try {
            Method method = annotation.getClass().getDeclaredMethod(attributeName);
            method.setAccessible(true);
            return method.invoke(annotation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
