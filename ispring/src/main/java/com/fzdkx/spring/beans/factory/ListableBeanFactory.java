package com.fzdkx.spring.beans.factory;

import com.fzdkx.spring.beans.exception.BeansException;

import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 定义了以下获取 Bean集合的方法
 */
public interface ListableBeanFactory extends BeanFactory{

    /**
     * 按照类型返回 Bean 实例
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回注册表中所有的Bean名称
     */
    String[] getBeanDefinitionNames();

}