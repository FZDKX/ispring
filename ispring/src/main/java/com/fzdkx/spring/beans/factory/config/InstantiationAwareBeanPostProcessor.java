package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * BeanPostProcessor：实例化Bean的AOP代理
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    Object getEarlyBeanReference(Object bean,String beanName,BeanDefinition bd);

}
