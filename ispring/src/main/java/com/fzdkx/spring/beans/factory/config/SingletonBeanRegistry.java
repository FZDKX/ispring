package com.fzdkx.spring.beans.factory.config;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 单例Bean的注册接口
 */
public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);

    // 注册单例Bean
    void registerSingleton(String beanName, Object singletonObject);
}
