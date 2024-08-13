package com.fzdkx.spring.context.event;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 上下文关闭事件：监听关闭上下文
 */
public class ContextClosedEvent extends ApplicationContextEvent{
    public ContextClosedEvent(Object source) {
        super(source);
    }
}
