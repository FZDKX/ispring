package com.fzdkx.spring.beans.factory;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.AutowireCapableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.ConfigurableBeanFactory;
import com.fzdkx.spring.util.StringValueResolver;

import java.util.List;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 可配置的集合BeanFactory
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    // 预实例化单例
    void preInstantiateSingletons() throws BeansException;

    // 根据类型获取Bean定义信息
    List<BeanDefinition> getBeanDefinitionByType(Class<?> type);
}