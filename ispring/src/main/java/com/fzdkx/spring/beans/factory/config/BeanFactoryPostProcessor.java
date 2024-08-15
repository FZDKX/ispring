package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 提供对BeanDefinition进行修改的能力
 */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
