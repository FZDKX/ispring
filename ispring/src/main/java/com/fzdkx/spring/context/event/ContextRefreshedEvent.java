package com.fzdkx.spring.context.event;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 上下文刷新事件：监听上下文刷新
 */
public class ContextRefreshedEvent extends ApplicationContextEvent{
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
