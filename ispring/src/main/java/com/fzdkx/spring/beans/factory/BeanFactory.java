package com.fzdkx.spring.beans.factory;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 核心接口：对Bean进行管理
 */
public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    // 根据名称获取Bean
    Object getBean(String name) throws BeansException;

    // 根据名称获取Bean，并转换为指定类型
    <T> T getBean(String name, Class<T> clazz) throws BeansException;

    // 判断一个Bean是否是单例
    boolean isSingleton(String name) throws BeansException;

    // 判断一个Bean是否是原型
    boolean isPrototype(String name) throws BeansException;

    // 容器中是否包含该Bean
    boolean containsBean(String name) throws BeansException;

    // 获取Bena的类型
    Class<?> getType(String name) throws BeansException;
}
