package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/16
 */
public abstract class InstantiationAwareBeanPostProcessor implements BeanPostProcessor{

    // 准备BeanPostProcess的必要组件
    public void prepareInstantiationAware() {
    }

    // 返回代理 或 源对象
    public Object getEarlyBeanReference(Object bean,String beanName,BeanDefinition bd){
        return bean;
    }

    // 在Bean 对象实例化完成后，设置属性操作之前执行此方法，可以对属性进行修改
    public BeanDefinition postProcessPropertyValues(BeanDefinition bd, Object bean, String beanName) throws BeansException {
        return bd;
    }

}
