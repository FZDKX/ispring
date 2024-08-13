package com.fzdkx.spring.util;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class ClassUtils {
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        // 获取当前线程 类加载器
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            // 获取失败，捕获
        }
        if (classLoader == null) {
            // 获取当前类的类加载器
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }
}
