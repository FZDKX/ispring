package com.fzdkx.spring.util;

import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.context.annotation.Component;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.HashSet;
import java.util.Set;

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

    // 判断是否是CGLIB生成的类
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    // 检查类名，判断是否是CGLIB生成的类
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

    public static Set<Class<?>> scanPackageByAnnotation(String basePackage, Class<? extends Annotation> annotation) {
        try {
            HashSet<Class<?>> set = new HashSet<>();
            // 获取指定路径下所有的类
            Set<Class<?>> classSet = getClassesInPackage(basePackage);
            // 获取含有 Component注解的类
            for (Class<?> clazz : classSet) {
                Annotation component = AnnotationUtils.getAnnotation(clazz, annotation);
                // 如果有，加入
                if (component != null) {
                    set.add(clazz);
                }
            }
            return set;
        } catch (ClassNotFoundException | NoSuchFileException e) {
            throw new RuntimeException("路径错误");
        }
    }

    public static Set<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException, NoSuchFileException {
        Set<Class<?>> classes = new HashSet<>();
        // 将包名转换为路径
        String path = packageName.replace('.', '/');
        // 获取当前类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 加载资源
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new NoSuchFileException("找不到指定路径");
        }
        // 获取包的路径
        File directory = new File(resource.getFile());
        if (directory.exists()) {
            // 递归获取所有类
            addClassesFromDirectory(directory, packageName, classes);
        }
        return classes;
    }

    private static void addClassesFromDirectory(File directory, String packageName, Set<Class<?>> classes) throws ClassNotFoundException {
        // 如果是目录
        if (directory.isDirectory()) {
            // 获取所有文件
            File[] files = directory.listFiles();
            if (files != null) {
                // 遍历
                for (File file : files) {
                    // 如果还是目录，递归
                    if (file.isDirectory()) {
                        // 递归查找子包
                        addClassesFromDirectory(file, packageName + "." + file.getName(), classes);
                    } else if (file.getName().endsWith(".class")) {
                        // 读取类文件
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
    }
}
