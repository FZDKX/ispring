package com.fzdkx.spring.context.annotation;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.ConfigurableBeanFactory;
import com.fzdkx.spring.beans.factory.support.BeanDefinitionRegistry;
import com.fzdkx.spring.util.AnnotationUtils;
import com.fzdkx.spring.util.StringUtils;

import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 包扫描注册BeanDefinition
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    // 扫描
    public void doScan(String... packages) {
        for (String p : packages) {
            Set<BeanDefinition> beanDefinitions = findCandidateComponents(p);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> clazz = beanDefinition.getBeanClass();
                // 解析 beanName
                String beanName = parseBeanName(clazz);
                // 解析 scope
                String scope = parseScope(clazz);
                // 解析 lazy
                boolean isLazy = parseLazy(clazz);
                beanDefinition.setName(beanName);
                beanDefinition.setScope(scope);
                beanDefinition.setLazyInit(isLazy);
                // 注册BeanDefinition
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }

    private boolean parseLazy(Class<?> clazz) {
        Lazy lazy = (Lazy) AnnotationUtils.getAnnotation(clazz, Lazy.class);
        if (lazy == null) {
            return false;
        } else {
            return lazy.value();
        }
    }

    private String parseScope(Class<?> clazz) {
        Scope scope = (Scope) AnnotationUtils.getAnnotation(clazz, Scope.class);
        if (scope == null) {
            return ConfigurableBeanFactory.SCOPE_SINGLETON;
        }
        String value = scope.value();
        if (!StringUtils.isEmpty(value)) {
            if (ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(value)) {
                value = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
            } else if (ConfigurableBeanFactory.SCOPE_SINGLETON.equals(value)) {
                value = ConfigurableBeanFactory.SCOPE_SINGLETON;
            } else {
                throw new BeansException("bean 作用域设置出错");
            }
        }
        return value;
    }

    private String parseBeanName(Class<?> clazz) {
        Component component = (Component) AnnotationUtils.getAnnotation(clazz, Component.class);
        if (component == null) {
            throw new BeansException("bean 解析异常");
        }
        String beanName = component.value();
        if (StringUtils.isEmpty(beanName)) {
            beanName = StringUtils.lowerFirst(clazz.getSimpleName());
        }
        return beanName;
    }
}
