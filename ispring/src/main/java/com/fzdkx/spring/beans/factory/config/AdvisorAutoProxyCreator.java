package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * BeanPostProcessor：实例化Bean的AOP代理
 */
public interface AdvisorAutoProxyCreator extends BeanPostProcessor{

    // AOP基础类初始化
    void initAop() throws BeansException;

    // 返回代理 或 源对象
    Object getEarlyBeanReference(Object bean,String beanName,BeanDefinition bd);

}
