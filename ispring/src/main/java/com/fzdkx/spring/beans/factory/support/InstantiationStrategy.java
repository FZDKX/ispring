package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * Bean的实例化策略接口
 */
public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException;

}
