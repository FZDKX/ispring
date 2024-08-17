package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.PropertyPlaceholderConfigurer;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.support.BeanDefinitionRegistry;
import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;
import com.fzdkx.spring.core.convert.support.ConversionServiceFactoryBean;
import com.fzdkx.spring.core.io.ResourceLoader;
import com.sun.istack.internal.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    // beanFactory
    private final DefaultListableBeanFactory beanFactory;

    @Nullable
    private ResourceLoader resourceLoader;

    private boolean customClassLoader = false;


    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    // 刷新bean容器
    protected void refreshBeanFactory() throws BeansException {
        // 注册类型转换器
        registerBeanDefinition(ConversionServiceFactoryBean.BEAN_NAME, new BeanDefinition(ConversionServiceFactoryBean.class));
        // 注册
        registerBeanDefinition(new BeanDefinition(PropertyPlaceholderConfigurer.class));
    }

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }


    @Override
    public List<String> getBeanNamesOfType(Class<Object> objectClass) {
        return this.beanFactory.getBeanNamesOfType(objectClass);
    }

    @Override
    public Set<BeanDefinition> getClassByAnnotation(Class<? extends Annotation> clazz) {
        return this.beanFactory.getClassByAnnotation(clazz);
    }

    @Override
    public List<BeanDefinition> getBeanDefinitionByType(Class<?> type) {
        return this.beanFactory.getBeanDefinitionByType(type);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        this.beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanFactory.containsBeanDefinition(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
