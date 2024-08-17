package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    private final ClassPathBeanDefinitionScanner scanner;

    public AnnotationConfigApplicationContext() {
        this.scanner = new ClassPathBeanDefinitionScanner(this);
        scanner.setBeanFactory(this.getBeanFactory());
    }

    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        this();
        register(componentClasses);
        refresh();
    }

    @Override
    public void register(Class<?>... componentClasses) {
        if (componentClasses == null || componentClasses.length == 0) {
            throw new BeansException("配置错误");
        }
        this.scanner.registerConfig(componentClasses);
    }

    @Override
    public void scan(String... basePackages) {
        this.scanner.doScan(basePackages);
    }
}
