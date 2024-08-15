package com.fzdkx.spring.context.annotation;

import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 进行类路径扫描，缺点候选组件
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 寻找 basePackage 路径下 加了Component注解的类
     * @param basePackage 类路径
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtils.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }

}
