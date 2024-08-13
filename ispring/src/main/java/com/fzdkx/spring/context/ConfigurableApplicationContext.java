package com.fzdkx.spring.context;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 可配置的应用上下文，提供刷新容器的接口方法
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    // 刷新容器方法
    void refresh() throws BeansException;

    // 注册JVM虚拟机关闭钩子
    void registerShutdownHook();

    // 关闭容器
    void close();
}
