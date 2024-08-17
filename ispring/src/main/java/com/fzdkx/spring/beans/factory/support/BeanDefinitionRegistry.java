package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanDefinitionStoreException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * BeanDefinition注册管理接口
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();
}
