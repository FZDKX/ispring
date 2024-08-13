package com.fzdkx.spring.beans.factory;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 可以获取 BeanName ，进行属性赋值
 */
public interface BeanNameAware extends Aware{
    void setBeanName(String beanName);
}
