package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanInstanceException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * JDK实例化
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Class<?> clazz = beanDefinition.getBeanClass();
        // 有指定构造方法
        try {
            if (ctor != null) {
                // 有参
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            }else {
                // 无参
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new BeanInstanceException("创建Bean出现错误");
        }
    }
}
