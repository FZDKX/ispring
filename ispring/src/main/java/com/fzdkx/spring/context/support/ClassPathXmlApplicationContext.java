package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{
    private String[] locations;

    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(String location) {
        this(new String[] {location});
    }

    public ClassPathXmlApplicationContext(String[] locations) throws BeansException {
        this.locations = locations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return this.locations;
    }

    @Override
    public List<String> getBeanNamesOfType(Class<Object> type) {
        return this.getBeanFactory().getBeanNamesOfType(type);
    }

    @Override
    public Set<BeanDefinition> getClassByAnnotation(Class<? extends Annotation> clazz) {
        return this.getBeanFactory().getClassByAnnotation(clazz);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        getBeanFactory().registerBeanDefinition(beanName,beanDefinition);
    }
}
