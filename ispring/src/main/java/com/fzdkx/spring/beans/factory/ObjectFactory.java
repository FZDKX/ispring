package com.fzdkx.spring.beans.factory;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 对象工厂，可以使用 getObject方法返回一个Bean：这个Bean可以是原始Bean，也可以是代理对象
 */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
