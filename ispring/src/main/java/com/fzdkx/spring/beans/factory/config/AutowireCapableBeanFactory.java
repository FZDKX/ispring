package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.BeanFactory;


/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 自动化处理Bean工厂配置的接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessBeforeInitialization 方法
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessorsAfterInitialization 方法
     */
    Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException;

}